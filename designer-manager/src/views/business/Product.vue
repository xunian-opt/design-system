<template>
  <div class="app-container">
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form" size="small">
        <el-form-item label="商品名称">
          <el-input v-model="queryParams.shangpinmingcheng" placeholder="请输入名称" clearable @clear="handleSearch"></el-input>
        </el-form-item>
        <el-form-item label="品牌">
          <el-input v-model="queryParams.shangpinpinpai" placeholder="请输入品牌" clearable @clear="handleSearch"></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="queryParams.shangpinfenlei" placeholder="请输入分类" clearable @clear="handleSearch"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card" shadow="never">
      <div class="toolbar">
        <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="el-icon-delete" size="small" :disabled="selection.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>

      <el-table 
        v-loading="loading" 
        :data="tableData" 
        border 
        style="width: 100%" 
        @selection-change="handleSelectionChange"
        size="medium"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column type="index" label="索引" width="60" align="center">
           <template slot-scope="scope">{{ (pagination.page - 1) * pagination.limit + scope.$index + 1 }}</template>
        </el-table-column>
        
        <el-table-column prop="shangpinmingcheng" label="商品名称" align="center" width="300" show-overflow-tooltip></el-table-column>
        
        <el-table-column prop="shangpinpinpai" label="商品品牌" align="center" min-width="80"></el-table-column>
        <el-table-column prop="shangpinfenlei" label="商品分类" align="center" min-width="80"></el-table-column>
        
        <el-table-column prop="shangpinguige" label="商品规格" align="center" width="120"></el-table-column>
        
        <el-table-column prop="price" label="价格" align="center" width="100">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">¥{{ scope.row.price }}</span>
          </template>
        </el-table-column>

        <el-table-column label="图片" align="center" width="120">
          <template slot-scope="scope">
            <div v-if="scope.row.shangpintupian">
               <el-image 
                style="width: 50px; height: 50px; border-radius: 4px; cursor: pointer;" 
                :src="getFirstImg(scope.row.shangpintupian)" 
                :preview-src-list="getImgList(scope.row.shangpintupian)"
                fit="cover">
              </el-image>
            </div>
            <span v-else style="color:#c0c4cc; font-size: 12px">暂无</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" class="blue-text" @click="handleEdit(scope.row)">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" class="red-text" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        style="margin-top: 20px; text-align: right;"
        @size-change="val => { pagination.limit = val; fetchData(); }"
        @current-change="val => { pagination.page = val; fetchData(); }"
        :current-page="pagination.page"
        :page-sizes="[10, 20, 50]"
        :page-size="pagination.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" @close="resetForm" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px" :disabled="mode === 'view'" size="small">
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="商品名称" prop="shangpinmingcheng">
              <el-input v-model="form.shangpinmingcheng" placeholder="请输入名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品品牌" prop="shangpinpinpai">
              <el-input v-model="form.shangpinpinpai" placeholder="请输商品品牌"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="商品分类" prop="shangpinfenlei">
              <el-select v-model="form.shangpinfenlei" placeholder="请选择商品分类" style="width: 100%">
                <el-option v-for="item in categoryOptions" :key="item.id" :label="item.shangpinfenlei" :value="item.shangpinfenlei"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品规格" prop="shangpinguige">
              <el-input v-model="form.shangpinguige" placeholder="例如：个/件/套"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="价格" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单次限购" prop="onelimittimes">
              <el-input-number v-model="form.onelimittimes" :min="-1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="总限购" prop="alllimittimes">
              <el-input-number v-model="form.alllimittimes" :min="-1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="优惠活动" prop="youhuihuodong">
          <el-input v-model="form.youhuihuodong" placeholder="请输入优惠信息"></el-input>
        </el-form-item>

        <el-form-item label="商品图片" prop="shangpintupian">
          <div v-if="mode === 'view' && (!fileList || fileList.length === 0)" style="color: #909399; font-size: 14px;">暂无图片</div>
          <el-upload
            v-else
            :action="uploadUrl"
            :headers="headers"
            list-type="picture-card"
            :file-list="fileList"
            :on-success="handleUploadSuccess"
            :on-remove="handleRemove"
            :limit="5"
            :class="{ 'hide-upload-btn': fileList.length >= 5 || mode === 'view' }"
            :disabled="mode === 'view'"
            accept=".jpg,.jpeg,.png"
          >
            <i class="el-icon-plus"></i>
            
            <template slot="file" slot-scope="{file}">
              <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
              <span class="el-upload-list__item-actions">
                <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
                  <i class="el-icon-zoom-in"></i>
                </span>
                <span v-if="mode !== 'view'" class="el-upload-list__item-delete" @click="handleRemove(file)">
                  <i class="el-icon-delete"></i>
                </span>
              </span>
            </template>
          </el-upload>
          <div class="el-upload__tip" v-if="mode !== 'view'">支持多图上传，最多5张，格式jpg/png</div>
        </el-form-item>

        <el-form-item label="商品详情" prop="shangpinxiangqing">
          <el-input type="textarea" :rows="4" v-model="form.shangpinxiangqing" placeholder="请输入商品详细描述"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" v-if="mode !== 'view'">
        <el-button @click="dialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="submitForm" size="small">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog :visible.sync="previewVisible" append-to-body>
      <img width="100%" :src="previewImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'Product',
  data() {
    return {
      loading: false,
      tableData: [],
      selection: [],
      total: 0,
      pagination: { page: 1, limit: 10 },
      queryParams: { 
        shangpinmingcheng: '',
        shangpinpinpai: '',
        shangpinfenlei: ''
      },
      dialogVisible: false,
      mode: 'add',
      categoryOptions: [],
      form: { 
        id: undefined, 
        shangpinmingcheng: '', 
        shangpinpinpai: '', 
        shangpinfenlei: '', 
        shangpinguige: '',
        youhuihuodong: '',
        onelimittimes: -1, 
        alllimittimes: -1,
        price: 0, 
        shangpintupian: '',
        shangpinxiangqing: ''
      },
      fileList: [],
      previewVisible: false,
      previewImageUrl: '',
      uploadUrl: '/springboot2z04j/file/upload',
      headers: { Token: localStorage.getItem('token') },
      
      rules: {
        shangpinmingcheng: [{ required: true, message: '必填', trigger: 'blur' }],
        shangpinpinpai: [{ required: true, message: '必填', trigger: 'blur' }],
        shangpinfenlei: [{ required: true, message: '必选', trigger: 'change' }],
        price: [{ required: true, message: '必填', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() { return { add: '新增商品', edit: '修改商品', view: '商品详情' }[this.mode] }
  },
  created() {
    this.fetchData()
    this.fetchCategories()
  },
  methods: {
    fetchData() {
      this.loading = true
      const params = {
        page: this.pagination.page,
        limit: this.pagination.limit,
        ...this.queryParams
      }
      Object.keys(params).forEach(key => (params[key] === '' || params[key] === null) && delete params[key]);

      request.get('/zhuangxiushangpin/page', { params }).then(res => {
        if (res && res.data) {
           this.tableData = res.data.list || []
           this.total = res.data.total || 0
        } else {
           this.tableData = []
           this.total = 0
        }
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    fetchCategories() {
      request.get('/shangpinfenlei/lists').then(res => { 
        if(res && res.data) {
            this.categoryOptions = res.data 
        }
      })
    },
    getImageUrl(img) {
      if (!img) return ''
      const baseUrl = '/springboot2z04j/upload/';
      return img.startsWith('http') ? img : baseUrl + img.replace(/^\//, '');
    },
    getFirstImg(imgs) {
      if (!imgs) return ''
      return this.getImageUrl(imgs.split(',')[0]);
    },
    getImgList(imgs) {
      if (!imgs) return []
      return imgs.split(',').map(f => this.getImageUrl(f))
    },
    
    handleSearch() { this.pagination.page = 1; this.fetchData() },
    resetSearch() { 
      this.queryParams = { shangpinmingcheng: '', shangpinpinpai: '', shangpinfenlei: '' }
      this.handleSearch() 
    },
    handleSelectionChange(val) { this.selection = val },
    
    initForm(row, mode) {
      this.mode = mode
      this.dialogVisible = true
      this.fileList = [] 

      if (mode === 'add') {
        this.form = { 
            id: undefined, 
            shangpinmingcheng: '', 
            shangpinpinpai: '', 
            shangpinfenlei: '', 
            shangpinguige: '',
            youhuihuodong: '',
            onelimittimes: -1,
            alllimittimes: -1,
            price: 0, 
            shangpintupian: '',
            shangpinxiangqing: ''
        }
      } else {
        this.form = { ...row }
        if(this.form.onelimittimes === undefined) this.$set(this.form, 'onelimittimes', -1);
        if(this.form.alllimittimes === undefined) this.$set(this.form, 'alllimittimes', -1);

        if (this.form.shangpintupian) {
          this.fileList = this.form.shangpintupian.split(',').map(f => ({
            name: f,
            url: this.getImageUrl(f)
          }))
        }
      }
      this.$nextTick(() => {
        if (this.$refs.form && mode !== 'view') this.$refs.form.clearValidate()
      })
    },
    handleAdd() { this.initForm(null, 'add') },
    handleEdit(row) { this.initForm(row, 'edit') },
    handleDetail(row) { this.initForm(row, 'view') },
    
    handleUploadSuccess(res, file) {
      if (res.code === 0) {
        const newFile = {
            name: res.file,
            url: this.getImageUrl(res.file)
        };
        this.fileList.push(newFile);
        this.$message.success('上传成功')
      } else {
        this.$message.error('上传失败')
      }
    },
    handleRemove(file) {
      const targetName = file.response ? file.response.file : file.name;
      this.fileList = this.fileList.filter(item => {
          const itemName = item.response ? item.response.file : item.name;
          return itemName !== targetName;
      });
    },
    handlePictureCardPreview(file) {
      this.previewImageUrl = file.url;
      this.previewVisible = true;
    },
    
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const names = this.fileList.map(f => {
              if (f.response && f.response.code === 0) return f.response.file; 
              return f.name;
          });
          this.form.shangpintupian = names.join(',')
          
          const url = this.mode === 'edit' ? '/zhuangxiushangpin/update' : '/zhuangxiushangpin/save'
          request.post(url, this.form).then(() => {
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.fetchData()
          })
        }
      })
    },
    resetForm() {
      this.fileList = []
      if (this.$refs.form) this.$refs.form.resetFields()
      this.form = { 
        id: undefined, 
        shangpinmingcheng: '', 
        shangpinpinpai: '', 
        shangpinfenlei: '', 
        shangpinguige: '',
        youhuihuodong: '',
        onelimittimes: -1,
        alllimittimes: -1,
        price: 0, 
        shangpintupian: '',
        shangpinxiangqing: ''
      }
    },
    handleDelete(id) {
       this.$confirm('确认删除?', '提示', { type: 'warning' }).then(() => {
         request.post('/zhuangxiushangpin/delete', [id]).then(() => {
           this.$message.success('删除成功'); this.fetchData()
         })
       }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.selection.map(i => i.id)
      this.$confirm(`确认删除 ${ids.length} 项?`, '提示', { type: 'warning' }).then(() => {
         request.post('/zhuangxiushangpin/delete', ids).then(() => {
           this.$message.success('删除成功'); this.fetchData()
         })
       }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 10px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);
}
.search-card {
  margin-bottom: 10px; 
  border-radius: 4px;
  border: none;
}
.search-form .el-form-item {
  margin-bottom: 0;
  margin-right: 15px;
}
.table-card {
  border-radius: 4px;
  border: none;
  min-height: 500px;
}
.toolbar { margin-bottom: 15px; }
.blue-text { color: #1890ff; }
.red-text { color: #ff4d4f; }
::v-deep .el-card__body { padding: 15px; }

::v-deep .hide-upload-btn .el-upload--picture-card { display: none; }
::v-deep .el-upload-list--picture-card .el-upload-list__item { transition: none !important; }
</style>
<template>
  <div class="app-container">
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form" size="small">
        <el-form-item label="作品名称">
          <el-input v-model="queryParams.zuopinmingcheng" placeholder="请输入作品名称" clearable @clear="handleSearch"></el-input>
        </el-form-item>
        <el-form-item label="风格类型">
          <el-input v-model="queryParams.fenggeleixing" placeholder="请输入风格" clearable @clear="handleSearch"></el-input>
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
        <el-table-column prop="zuopinmingcheng" label="作品名称" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="shejishixingming" label="设计师" align="center" width="120"></el-table-column>
        <el-table-column prop="fenggeleixing" label="风格" align="center" width="120"></el-table-column>
        <el-table-column prop="faburiqi" label="发布日期" align="center" width="120">
           <template slot-scope="scope">{{ formatDate(scope.row.faburiqi) }}</template>
        </el-table-column>
        <el-table-column label="封面" align="center" width="120">
          <template slot-scope="scope">
             <div v-if="scope.row.fengmianxinxi">
               <el-image 
                style="width: 60px; height: 40px; border-radius: 4px; cursor: pointer;" 
                :src="getImageUrl(scope.row.fengmianxinxi)" 
                :preview-src-list="getImgList(scope.row.fengmianxinxi)"
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
            <el-form-item label="作品名称" prop="zuopinmingcheng">
              <el-input v-model="form.zuopinmingcheng"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设计师" prop="shejishixingming">
              <el-select v-model="form.shejishixingming" placeholder="请选择设计师" style="width: 100%">
                <el-option v-for="item in designerOptions" :key="item.id" :label="item.xingming" :value="item.xingming"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="风格类型" prop="fenggeleixing">
              <el-input v-model="form.fenggeleixing"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发布日期" prop="faburiqi">
              <el-date-picker 
                v-model="form.faburiqi" 
                type="date" 
                value-format="yyyy-MM-dd"
                placeholder="选择日期" 
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="封面图片" prop="fengmianxinxi">
          <div v-if="mode === 'view' && (!fileList || fileList.length === 0)" style="color: #909399; font-size: 14px;">暂无图片</div>
          <el-upload
            v-else
            :action="uploadUrl"
            :headers="headers"
            list-type="picture-card"
            :file-list="fileList"
            :on-success="handleUploadSuccess"
            :on-remove="handleRemove"
            :limit="1"
            :class="{ 'hide-upload-btn': fileList.length >= 1 || mode === 'view' }"
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
        </el-form-item>

        <el-form-item label="方案介绍" prop="fanganjieshao">
          <div v-if="mode === 'view' && (!form.fanganjieshao || form.fanganjieshao === 'null')" style="color: #909399; font-size: 14px;">暂无数据</div>
          
          <el-upload
            v-else
            class="upload-pdf"
            :action="uploadUrl"
            :headers="headers"
            :limit="1"
            :file-list="fanganList"
            :on-success="handleFanganSuccess"
            :on-remove="handleFanganRemove"
            :before-upload="beforePdfUpload"
            :on-preview="handlePdfPreview"
            accept=".pdf"
            :disabled="mode === 'view'">
            <el-button size="small" type="primary" v-if="mode !== 'view'">点击上传PDF</el-button>
            </el-upload>
        </el-form-item>

        <el-form-item label="内容详情" prop="neirongxiangqing">
          <div v-if="mode === 'view' && (!form.neirongxiangqing || form.neirongxiangqing === 'null')" style="color: #909399; font-size: 14px;">暂无数据</div>
          
          <el-upload
            v-else
            class="upload-pdf"
            :action="uploadUrl"
            :headers="headers"
            :limit="1"
            :file-list="neirongList"
            :on-success="handleNeirongSuccess"
            :on-remove="handleNeirongRemove"
            :before-upload="beforePdfUpload"
            :on-preview="handlePdfPreview"
            accept=".pdf"
            :disabled="mode === 'view'">
            <el-button size="small" type="primary" v-if="mode !== 'view'">点击上传PDF</el-button>
            </el-upload>
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
  name: 'Work',
  data() {
    return {
      loading: false,
      tableData: [],
      selection: [],
      total: 0,
      pagination: { page: 1, limit: 10 },
      queryParams: { 
        zuopinmingcheng: '',
        fenggeleixing: '' 
      },
      dialogVisible: false,
      mode: 'add',
      designerOptions: [],
      form: { 
        id: undefined, 
        zuopinmingcheng: '', 
        shejishixingming: '', 
        fenggeleixing: '', 
        fengmianxinxi: '',
        fanganjieshao: '', 
        neirongxiangqing: '', 
        faburiqi: ''
      },
      fileList: [],
      previewVisible: false,
      previewImageUrl: '',
      fanganList: [],
      neirongList: [],
      uploadUrl: '/springboot2z04j/file/upload',
      headers: { Token: localStorage.getItem('token') },
      rules: {
        zuopinmingcheng: [{ required: true, message: '必填', trigger: 'blur' }],
        shejishixingming: [{ required: true, message: '必选', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogTitle() { return { add: '新增作品', edit: '修改作品', view: '作品详情' }[this.mode] }
  },
  created() {
    this.fetchData()
    this.fetchDesigners()
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

      request.get('/zhuangxiuzuopin/page', { params }).then(res => {
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
    fetchDesigners() {
      request.get('/yuangongxinxi/lists').then(res => { 
        if(res && res.data) this.designerOptions = res.data 
      })
    },
    
    getImageUrl(img) {
      if (!img) return ''
      const baseUrl = '/springboot2z04j/upload/';
      return img.startsWith('http') ? img : baseUrl + img.replace(/^\//, '');
    },
    getImgList(imgs) {
      if (!imgs) return []
      return imgs.split(',').map(f => this.getImageUrl(f))
    },
    getFirstImg(imgs) {
        if (!imgs) return ''
        return this.getImageUrl(imgs.split(',')[0]);
    },
    formatDate(dateStr) {
        if (!dateStr) return '';
        if (typeof dateStr === 'string') {
            return dateStr.substring(0, 10);
        }
        const date = new Date(dateStr);
        const y = date.getFullYear();
        const m = (date.getMonth() + 1).toString().padStart(2, '0');
        const d = date.getDate().toString().padStart(2, '0');
        return `${y}-${m}-${d}`;
    },

    handleSearch() { this.pagination.page = 1; this.fetchData() },
    resetSearch() { 
      this.queryParams = { zuopinmingcheng: '', fenggeleixing: '' }
      this.handleSearch() 
    },
    handleSelectionChange(val) { this.selection = val },
    
    initForm(row, mode) {
      this.mode = mode
      this.dialogVisible = true
      this.fileList = []
      this.fanganList = []
      this.neirongList = []

      if (mode === 'add') {
        this.form = { 
            id: undefined, 
            zuopinmingcheng: '', 
            shejishixingming: '', 
            fenggeleixing: '', 
            fengmianxinxi: '',
            fanganjieshao: '',
            neirongxiangqing: '',
            faburiqi: new Date().toISOString().substring(0,10) 
        }
      } else {
        this.form = { ...row }
        if (this.form.fengmianxinxi) {
          this.fileList = this.form.fengmianxinxi.split(',').map(f => ({
            name: f,
            url: this.getImageUrl(f)
          }))
        }
        if (this.form.fanganjieshao && this.form.fanganjieshao !== 'null' && this.form.fanganjieshao.trim() !== '') {
            this.fanganList = [{ name: this.form.fanganjieshao, url: this.getImageUrl(this.form.fanganjieshao) }];
        } else {
            this.form.fanganjieshao = '';
        }
        if (this.form.neirongxiangqing && this.form.neirongxiangqing !== 'null' && this.form.neirongxiangqing.trim() !== '') {
            this.neirongList = [{ name: this.form.neirongxiangqing, url: this.getImageUrl(this.form.neirongxiangqing) }];
        } else {
            this.form.neirongxiangqing = '';
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
        const newFile = { name: res.file, url: this.getImageUrl(res.file) };
        this.fileList = [newFile]; 
        this.$message.success('上传成功')
      } else { this.$message.error('上传失败') }
    },
    handleRemove(file) { this.fileList = [] },
    handlePictureCardPreview(file) {
      this.previewImageUrl = file.url;
      this.previewVisible = true;
    },

    beforePdfUpload(file) {
        const isPDF = file.type === 'application/pdf';
        if (!isPDF) {
          this.$message.error('只能上传 PDF 格式的文件!');
        }
        return isPDF;
    },
    handlePdfPreview(file) {
        if(file.url) {
            window.open(file.url, '_blank');
        }
    },
    handleFanganSuccess(res, file) {
        if(res.code === 0) {
            this.form.fanganjieshao = res.file;
            this.fanganList = [{ name: res.file, url: this.getImageUrl(res.file) }];
            this.$message.success('上传成功');
        } else {
            this.$message.error('上传失败');
        }
    },
    handleFanganRemove(file) {
        this.form.fanganjieshao = '';
        this.fanganList = [];
    },
    handleNeirongSuccess(res, file) {
        if(res.code === 0) {
            this.form.neirongxiangqing = res.file;
            this.neirongList = [{ name: res.file, url: this.getImageUrl(res.file) }];
            this.$message.success('上传成功');
        } else {
            this.$message.error('上传失败');
        }
    },
    handleNeirongRemove(file) {
        this.form.neirongxiangqing = '';
        this.neirongList = [];
    },
    
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if(this.fileList.length > 0) {
              const file = this.fileList[0];
              this.form.fengmianxinxi = (file.response && file.response.code===0) ? file.response.file : file.name;
          } else {
              this.form.fengmianxinxi = '';
          }
          
          const url = this.mode === 'edit' ? '/zhuangxiuzuopin/update' : '/zhuangxiuzuopin/save'
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
      this.fanganList = []
      this.neirongList = []
      if (this.$refs.form) this.$refs.form.resetFields()
      this.form = { 
          id: undefined, 
          zuopinmingcheng: '', 
          shejishixingming: '', 
          fenggeleixing: '', 
          fengmianxinxi: '',
          fanganjieshao: '',
          neirongxiangqing: '',
          faburiqi: ''
      }
    },
    handleDelete(id) {
       this.$confirm('确认删除?', '提示', { type: 'warning' }).then(() => {
         request.post('/zhuangxiuzuopin/delete', [id]).then(() => {
           this.$message.success('删除成功'); this.fetchData()
         })
       }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.selection.map(i => i.id)
      this.$confirm(`确认删除 ${ids.length} 项?`, '提示', { type: 'warning' }).then(() => {
         request.post('/zhuangxiuzuopin/delete', ids).then(() => {
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

.upload-pdf {
    width: 100%;
}
</style>
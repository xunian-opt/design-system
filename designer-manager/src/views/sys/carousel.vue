<template>
  <div class="app-container">
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form" size="small">
        <el-form-item label="轮播图名称">
          <el-input v-model="queryParams.name" placeholder="请输入轮播图名称" clearable @clear="handleSearch"></el-input>
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
        <el-table-column type="index" label="索引" width="80" align="center">
           <template slot-scope="scope">{{ (pagination.page - 1) * pagination.limit + scope.$index + 1 }}</template>
        </el-table-column>
        <el-table-column prop="name" label="轮播图名称" align="center"></el-table-column>
        
        <el-table-column label="图片" align="center" width="120">
          <template slot-scope="scope">
             <div v-if="scope.row.picture">
               <el-image 
                style="width: 80px; height: 50px; border-radius: 4px; cursor: pointer;" 
                :src="getImageUrl(scope.row.picture)" 
                :preview-src-list="[getImageUrl(scope.row.picture)]"
                fit="cover">
              </el-image>
            </div>
            <span v-else style="color:#c0c4cc; font-size: 12px">暂无</span>
          </template>
        </el-table-column>

        <el-table-column prop="url" label="跳转链接" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="sort" label="排序" align="center" width="80"></el-table-column>
        
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="resetForm" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px" size="small">
        <el-form-item label="轮播图名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入轮播图名称"></el-input>
        </el-form-item>
        
        <el-form-item label="轮播图片" prop="picture">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :headers="headers"
            list-type="picture-card"
            :file-list="fileList"
            :on-success="handleUploadSuccess"
            :on-remove="handleRemove"
            :on-preview="handlePictureCardPreview"
            :before-upload="beforeAvatarUpload"
            :limit="1"
            :class="{ 'hide-upload-btn': fileList.length >= 1 }"
            accept=".jpg,.jpeg,.png"
          >
            <i class="el-icon-plus"></i>
            <div slot="tip" class="el-upload__tip">只能上传 jpg/png 文件，且不超过 2MB</div>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="跳转链接" prop="url">
          <el-input v-model="form.url" placeholder="可选，点击图片后的跳转地址"></el-input>
        </el-form-item>
        
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" label="排序值"></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
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
  name: 'Carousel', // 修改组件名称
  data() {
    return {
      loading: false,
      tableData: [],
      selection: [],
      total: 0,
      pagination: { page: 1, limit: 10 },
      queryParams: { name: '' },
      dialogVisible: false,
      mode: 'add',
      // 修改字段结构：picture 替代 value，增加 sort
      form: { id: '', name: '', picture: '', url: '', sort: 0 },
      
      // 上传相关数据
      fileList: [],
      previewVisible: false,
      previewImageUrl: '',
      uploadUrl: '/springboot2z04j/file/upload',
      headers: { Token: localStorage.getItem('token') },
      
      rules: {
        name: [{ required: true, message: '必填', trigger: 'blur' }],
        picture: [{ required: true, message: '请上传图片', trigger: 'change' }] // 字段名修正
      }
    }
  },
  computed: {
    dialogTitle() { return this.mode === 'add' ? '新增轮播图' : '修改轮播图' }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      // 接口地址修正为 /carousel/page
      request.get('/carousel/page', {
        params: { page: this.pagination.page, limit: this.pagination.limit, ...this.queryParams }
      }).then(res => {
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
    
    getImageUrl(img) {
      if (!img) return ''
      const baseUrl = '/springboot2z04j/upload/';
      return img.startsWith('http') ? img : baseUrl + img.replace(/^\//, '');
    },

    handleSearch() { this.pagination.page = 1; this.fetchData() },
    resetSearch() { this.queryParams.name = ''; this.handleSearch() },
    handleSelectionChange(val) { this.selection = val },
    
    initForm(row, mode) {
      this.mode = mode
      this.dialogVisible = true
      this.fileList = []

      if (mode === 'add') {
        this.form = { name: '', picture: '', url: '', sort: 0 }
      } else {
        this.form = { ...row }
        // 修正字段回显逻辑：使用 picture
        if (this.form.picture) {
            this.fileList = [{ name: this.form.picture, url: this.getImageUrl(this.form.picture) }];
        }
      }
      this.$nextTick(() => {
        if (this.$refs.form) this.$refs.form.clearValidate()
      })
    },
    handleAdd() { this.initForm(null, 'add') },
    handleEdit(row) { this.initForm(row, 'edit') },

    // --- 上传相关方法 ---
    handleUploadSuccess(res, file) {
      if (res.code === 0) {
        // 修正赋值：picture
        this.form.picture = res.file
        this.fileList = [{ name: res.file, url: this.getImageUrl(res.file) }];
        this.$refs.form.clearValidate('picture'); // 修正校验字段
        this.$message.success('上传成功')
      } else {
        this.$message.error(res.msg || '上传失败')
        this.fileList = []
      }
    },
    handleRemove(file) {
      this.form.picture = '' // 修正赋值
      this.fileList = []
    },
    handlePictureCardPreview(file) {
      this.previewImageUrl = file.url;
      this.previewVisible = true;
    },
    beforeAvatarUpload(file) {
      const isJPGOrPNG = ['image/jpeg', 'image/png'].includes(file.type);
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPGOrPNG) {
        this.$message.error('上传图片只能是 JPG/PNG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!');
      }
      return isJPGOrPNG && isLt2M;
    },
    // ------------------
    
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 修正校验逻辑
          if (!this.form.picture) {
              this.$message.error('请上传图片');
              return;
          }
          
          // 接口地址修正
          const url = this.mode === 'edit' ? '/carousel/update' : '/carousel/save'
          request.post(url, this.form).then(res => {
            if (res.code === 0) {
                this.$message.success('操作成功')
                this.dialogVisible = false
                this.fetchData()
            } else {
                this.$message.error(res.msg || '操作失败')
            }
          })
        }
      })
    },
    resetForm() {
      this.fileList = [] 
      if (this.$refs.form) this.$refs.form.resetFields()
      this.form = { id: '', name: '', picture: '', url: '', sort: 0 }
    },
    handleDelete(id) {
       this.$confirm('确认删除?', '提示', { type: 'warning' }).then(() => {
         // 接口地址修正
         request.post('/carousel/delete', [id]).then(res => {
           if (res.code === 0) {
             this.$message.success('删除成功'); this.fetchData()
           } else {
             this.$message.error(res.msg || '删除失败')
           }
         })
       }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.selection.map(i => i.id)
      this.$confirm(`确认删除 ${ids.length} 项?`, '提示', { type: 'warning' }).then(() => {
         // 接口地址修正
         request.post('/carousel/delete', ids).then(res => {
           if (res.code === 0) {
             this.$message.success('删除成功'); this.fetchData()
           } else {
             this.$message.error(res.msg || '删除失败')
           }
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

/* 控制上传按钮隐藏：达到1张上限时隐藏 */
::v-deep .hide-upload-btn .el-upload--picture-card {
    display: none;
}
/* 移除过渡动画防止闪烁 */
::v-deep .el-upload-list--picture-card .el-upload-list__item {
    transition: none !important;
}
</style>
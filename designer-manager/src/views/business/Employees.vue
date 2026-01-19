<template>
  <div class="app-container">
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form" size="small">
        <el-form-item label="设计师工号">
          <el-input v-model="queryParams.gonghao" placeholder="请输入工号" clearable @clear="handleSearch"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="queryParams.xingming" placeholder="请输入姓名" clearable @clear="handleSearch"></el-input>
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
          <template slot-scope="scope">
            {{ (pagination.page - 1) * pagination.limit + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="gonghao" label="工号" align="center" width="100"></el-table-column>
        <el-table-column prop="xingming" label="姓名" align="center" width="100"></el-table-column>
        <el-table-column prop="xingbie" label="性别" width="70" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.xingbie === '男' ? '' : 'danger'" size="mini" effect="plain">{{ scope.row.xingbie }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="头像" align="center" width="90">
          <template slot-scope="scope">
            <el-image 
              v-if="scope.row.touxiang"
              style="width: 50px; height: 50px; border-radius: 4px; cursor: pointer;"
              :src="getImageUrl(scope.row.touxiang)"
              :preview-src-list="[getImageUrl(scope.row.touxiang)]"
              fit="cover">
            </el-image>
            <span v-else style="color:#c0c4cc; font-size: 12px">暂无</span>
          </template>
        </el-table-column>

        <el-table-column prop="zhiwei" label="职位" align="center" min-width="100"></el-table-column>
        <el-table-column prop="fuzexiangmu" label="负责项目" align="center" min-width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="lianxifangshi" label="电话" align="center" width="120"></el-table-column>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" @close="resetForm" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="90px" :disabled="mode === 'view'" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工号" prop="gonghao">
              <el-input v-model="form.gonghao" placeholder="请输入工号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="xingming">
              <el-input v-model="form.xingming" placeholder="请输入姓名"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="xingbie">
              <el-radio-group v-model="form.xingbie">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话" prop="lianxifangshi">
              <el-input v-model="form.lianxifangshi" placeholder="请输入电话"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职位" prop="zhiwei">
              <el-input v-model="form.zhiwei" placeholder="请输入职位"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="负责项目" prop="fuzexiangmu">
          <el-input type="textarea" :rows="2" v-model="form.fuzexiangmu" placeholder="请输入负责项目"></el-input>
        </el-form-item>

        <el-form-item label="头像" prop="touxiang">
          <div v-if="mode === 'view' && fileList.length === 0" style="color: #909399; font-size: 14px;">暂无头像</div>
          <el-upload
            v-else
            :action="uploadUrl"
            :headers="headers"
            list-type="picture-card"
            :file-list="fileList"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeAvatarUpload"
            :limit="1"
            :class="{ 'hide-upload-btn': fileList.length >= 1 || mode === 'view' }"
            :disabled="mode === 'view'"
            accept=".jpg,.jpeg,.png">
            
            <i class="el-icon-plus"></i>

            <template slot="file" slot-scope="{file}">
              <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
              <span class="el-upload-list__item-actions">
                <span
                  class="el-upload-list__item-preview"
                  @click="handlePictureCardPreview(file)"
                >
                  <i class="el-icon-zoom-in"></i>
                </span>
                <span
                  v-if="mode !== 'view'"
                  class="el-upload-list__item-delete"
                  @click="handleRemove(file)"
                >
                  <i class="el-icon-delete"></i>
                </span>
              </span>
            </template>
          </el-upload>
          <div class="el-upload__tip" v-if="mode !== 'view'">只能上传jpg/png文件，且不超过2MB，限一张</div>
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
  name: 'Employees',
  data() {
    return {
      loading: false,
      tableData: [],
      selection: [],
      total: 0,
      pagination: {
        page: 1,
        limit: 10
      },
      queryParams: {
        gonghao: '',
        xingming: ''
      },
      dialogVisible: false,
      mode: 'add', 
      form: {
        id: undefined,
        gonghao: '',
        xingming: '',
        xingbie: '男',
        zhiwei: '',
        lianxifangshi: '',
        fuzexiangmu: '',
        touxiang: '' 
      },
      fileList: [],
      previewVisible: false,
      previewImageUrl: '',

      rules: {
        gonghao: [{ required: true, message: '请输入工号', trigger: 'blur' }],
        xingming: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
      },
      uploadUrl: '/springboot2z04j/file/upload',
      headers: { Token: localStorage.getItem('token') }
    }
  },
  computed: {
    dialogTitle() {
      const map = { add: '新增设计师', edit: '修改设计师', view: '设计师详情' }
      return map[this.mode]
    }
  },
  created() {
    this.fetchData()
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

      request.get('/yuangongxinxi/page', { params }).then(res => {
        if (res && res.data) {
           this.tableData = res.data.list || []
           this.total = res.data.total || 0
        } else {
           this.tableData = []
           this.total = 0
        }
        this.loading = false
      }).catch(err => {
        console.error(err)
        this.loading = false
      })
    },
    getImageUrl(img) {
      if (!img) return ''
      const baseUrl = '/springboot2z04j/upload/';
      return img.startsWith('http') ? img : baseUrl + img.replace(/^\//, '');
    },
    handleSearch() {
      this.pagination.page = 1
      this.fetchData()
    },
    resetSearch() {
      this.queryParams = { gonghao: '', xingming: '' }
      this.handleSearch()
    },
    handleSelectionChange(val) {
      this.selection = val
    },
    
    initForm(row, mode) {
      this.mode = mode
      this.dialogVisible = true
      // 打开时立即清空列表，防止闪烁
      this.fileList = [];

      if (mode === 'add') {
        this.form = {
          id: undefined,
          gonghao: '',
          xingming: '',
          xingbie: '男',
          zhiwei: '',
          lianxifangshi: '',
          fuzexiangmu: '', 
          touxiang: ''
        }
      } else {
        this.form = { ...row }
        this.$set(this.form, 'fuzexiangmu', row.fuzexiangmu || '')
        
        if (this.form.touxiang) {
            this.fileList = [{
                name: this.form.touxiang, 
                url: this.getImageUrl(this.form.touxiang) 
            }];
        }
      }
      
      this.$nextTick(() => {
        if (this.$refs.form && mode !== 'view') {
           this.$refs.form.clearValidate();
        }
      })
    },
    handleAdd() { this.initForm(null, 'add') },
    handleEdit(row) { this.initForm(row, 'edit') },
    handleDetail(row) { this.initForm(row, 'view') },

    handleUploadSuccess(res, file) {
      if (res.code === 0) {
        this.fileList = [{
            name: res.file, 
            url: this.getImageUrl(res.file) 
        }];
        this.$refs.form.clearValidate('touxiang');
        this.$message.success('上传成功')
      } else {
        this.$message.error(res.msg || '上传失败');
        this.fileList = [];
      }
    },
    handleUploadError() {
      this.$message.error('网络错误，上传失败');
      this.fileList = [];
    },
    handleRemove(file) {
      this.fileList = [];
    },
    handlePictureCardPreview(file) {
      this.previewImageUrl = file.url;
      this.previewVisible = true;
    },

    beforeAvatarUpload(file) {
      const isJPGOrPNG = ['image/jpeg', 'image/png'].includes(file.type);
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPGOrPNG) this.$message.error('上传头像图片只能是 JPG/PNG 格式!');
      if (!isLt2M) this.$message.error('上传头像图片大小不能超过 2MB!');
      return isJPGOrPNG && isLt2M;
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.fileList.length > 0) {
              this.form.touxiang = this.fileList[0].name;
          } else {
              this.form.touxiang = ''; 
          }

          const url = this.mode === 'edit' ? '/yuangongxinxi/update' : '/yuangongxinxi/save'
          request.post(url, this.form).then(res => {
            this.$message.success(this.mode === 'edit' ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.fetchData()
          })
        }
      })
    },
    // 【Bug修复】重置表单时显式清空 fileList
    resetForm() {
      this.fileList = []; 
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }
      this.form = {
          id: undefined,
          gonghao: '',
          xingming: '',
          xingbie: '男',
          zhiwei: '',
          lianxifangshi: '',
          fuzexiangmu: '',
          touxiang: ''
      };
    },
    handleDelete(id) {
      this.$confirm('确认删除该设计师?', '提示', { type: 'warning' }).then(() => {
        request.post('/yuangongxinxi/delete', [id]).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    handleBatchDelete() {
      const ids = this.selection.map(item => item.id)
      this.$confirm(`确认删除选中的 ${ids.length} 名设计师?`, '提示', { type: 'warning' }).then(() => {
        request.post('/yuangongxinxi/delete', ids).then(() => {
          this.$message.success('批量删除成功')
          this.fetchData()
        })
      })
    }
  }
}
</script>

<style scoped>
.app-container {
  /* 调整：减少整体内边距，适应紧凑布局 */
  padding: 10px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);
}
.search-card {
  /* 调整：减少下方间距，实现“红圈间隔紧凑” */
  margin-bottom: 10px; 
  border-radius: 4px;
  border: none;
}
.search-form .el-form-item {
  /* 调整：移除表单项底部间距 */
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

/* 调整卡片内部间距 */
::v-deep .el-card__body {
  padding: 15px;
}

/* 控制上传按钮隐藏 */
::v-deep .hide-upload-btn .el-upload--picture-card {
    display: none;
}
::v-deep .el-upload-list--picture-card .el-upload-list__item {
    transition: none !important;
}
</style>
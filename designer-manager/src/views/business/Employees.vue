<template>
  <div class="app-container">
  <!--  <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item>业务管理</el-breadcrumb-item>
      <el-breadcrumb-item>设计师信息管理</el-breadcrumb-item>
    </el-breadcrumb> -->

    <el-card>
      <el-form :inline="true" :model="queryParams" class="search-form">
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

      <div class="toolbar">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="el-icon-delete" :disabled="selection.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>

      <el-table 
        v-loading="loading" 
        :data="tableData" 
        border 
        style="width: 100%" 
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column type="index" label="索引" width="80" align="center">
          <template slot-scope="scope">
            {{ (pagination.page - 1) * pagination.limit + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="gonghao" label="工号" align="center"></el-table-column>
        <el-table-column prop="xingming" label="姓名" align="center"></el-table-column>
        <el-table-column prop="xingbie" label="性别" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.xingbie === '男' ? '' : 'danger'" size="small">{{ scope.row.xingbie }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="zhiwei" label="职位" align="center"></el-table-column>
        <el-table-column prop="lianxifangshi" label="电话" align="center"></el-table-column>
        <el-table-column label="照片" align="center" width="100">
          <template slot-scope="scope">
            <el-image 
              v-if="scope.row.touxiang"
              style="width: 40px; height: 40px; border-radius: 4px;"
              :src="getImageUrl(scope.row.touxiang)"
              :preview-src-list="[getImageUrl(scope.row.touxiang)]">
            </el-image>
            <span v-else style="color:#909399; font-size: 12px">暂无</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="handleDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">修改</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        style="margin-top: 20px; text-align: center;"
        @size-change="val => { pagination.limit = val; fetchData(); }"
        @current-change="val => { pagination.page = val; fetchData(); }"
        :current-page="pagination.page"
        :page-sizes="[10, 20, 50]"
        :page-size="pagination.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px" :disabled="mode === 'view'">
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
              <el-select v-model="form.xingbie" placeholder="请选择" style="width: 100%">
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话" prop="lianxifangshi">
              <el-input v-model="form.lianxifangshi" placeholder="请输入电话"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="职位" prop="zhiwei">
          <el-input v-model="form.zhiwei" placeholder="请输入职位"></el-input>
        </el-form-item>

        <el-form-item label="头像" prop="touxiang">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :headers="headers"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :disabled="mode === 'view'">
            <img v-if="form.touxiang" :src="getImageUrl(form.touxiang)" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div slot="tip" class="el-upload__tip" v-if="mode !== 'view'">只能上传jpg/png文件，且不超过2MB</div>
          </el-upload>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer" v-if="mode !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
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
      // 弹窗相关
      dialogVisible: false,
      mode: 'add', // add, edit, view
      form: {
        id: undefined,
        gonghao: '',
        xingming: '',
        xingbie: '男',
        zhiwei: '',
        lianxifangshi: '',
        touxiang: ''
      },
      rules: {
        gonghao: [{ required: true, message: '请输入工号', trigger: 'blur' }],
        xingming: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
      },
      // 上传配置
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
    // 获取列表
        fetchData() {
          this.loading = true
          
          // 1. 定义基础分页参数
          const params = {
            page: this.pagination.page,
            limit: this.pagination.limit
          }
    
          // 2. 动态添加查询参数（只有当值存在且不为空时才添加）
          if (this.queryParams.gonghao) {
            params.gonghao = this.queryParams.gonghao
          }
          if (this.queryParams.xingming) {
            params.xingming = this.queryParams.xingming
          }
    
          request.get('/yuangongxinxi/page', {
            params: params
          }).then(res => {
            // 3. 兼容后端返回结构
            // 这里的 res 已经被 request.js 拦截器处理过，如果是直接返回 response.data
            // 请确保结构是 { code: 0, data: { list: [], total: 0 } }
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
    // 图片地址处理
    getImageUrl(img) {
      if (!img) return ''
      return '/springboot2z04j/upload/' + img
    },
    // 搜索
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
    // 初始化表单
    initForm(row, mode) {
      this.mode = mode
      this.dialogVisible = true
      if (mode === 'add') {
        this.form = {
          id: undefined,
          gonghao: '',
          xingming: '',
          xingbie: '男',
          zhiwei: '',
          lianxifangshi: '',
          touxiang: ''
        }
      } else {
        this.form = { ...row }
      }
    },
    handleAdd() {
      this.initForm(null, 'add')
    },
    handleEdit(row) {
      this.initForm(row, 'edit')
    },
    handleDetail(row) {
      this.initForm(row, 'view')
    },
    // 上传成功回调
    handleAvatarSuccess(res, file) {
      if (res.code === 0) {
        this.form.touxiang = res.file
        this.$message.success('上传成功')
      } else {
        this.$message.error(res.msg || '上传失败')
      }
    },
    // 上传前校验
    beforeAvatarUpload(file) {
      const isJPGOrPNG = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPGOrPNG) {
        this.$message.error('上传头像图片只能是 JPG/PNG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPGOrPNG && isLt2M;
    },
    // 提交保存
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const url = this.mode === 'edit' ? '/yuangongxinxi/update' : '/yuangongxinxi/save'
          request.post(url, this.form).then(() => {
            this.$message.success(this.mode === 'edit' ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.fetchData()
          })
        }
      })
    },
    // 重置表单
    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    // 删除
    handleDelete(id) {
      this.$confirm('确认删除该设计师?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.post('/yuangongxinxi/delete', [id]).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    // 批量删除
    handleBatchDelete() {
      const ids = this.selection.map(item => item.id)
      this.$confirm(`确认删除选中的 ${ids.length} 名设计师?`, '提示', {
        type: 'warning'
      }).then(() => {
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
.breadcrumb { margin-bottom: 20px; }
.toolbar { margin-bottom: 20px; }
.search-form { margin-bottom: 10px; }

/* 头像上传样式 */
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  object-fit: cover;
  border-radius: 6px;
}
</style>
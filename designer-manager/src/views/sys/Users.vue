<template>
  <div class="app-container">
    <!-- <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
    </el-breadcrumb> -->

    <el-card>
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable @clear="fetchData"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-input v-model="queryParams.role" placeholder="请输入角色" clearable @clear="fetchData"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
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
        style="width: 100%; margin-top: 20px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column type="index" label="索引" width="80" align="center">
          <template slot-scope="scope">
            {{ (pagination.page - 1) * pagination.limit + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" align="center"></el-table-column>
        <el-table-column prop="role" label="角色" align="center">
          <template slot-scope="scope">
            <el-tag size="small">{{ scope.row.role }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="mobile" label="手机" align="center"></el-table-column>
        <el-table-column label="操作" width="300" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">修改</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
            <el-button size="mini" type="warning" icon="el-icon-key" @click="handleResetPwd(scope.row)">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        style="margin-top: 20px; text-align: center;"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.page"
        :page-sizes="[10, 20, 50]"
        :page-size="pagination.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="resetDialog">
      <el-form :model="form" :rules="rules" ref="dataForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :disabled="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roleOptions" :key="item.id" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="手机" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
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
        username: '',
        role: ''
      },
      roleOptions: [], // 角色列表
      // Dialog 数据
      dialogVisible: false,
      isEdit: false,
      form: {
        id: undefined,
        username: '',
        password: '',
        role: '',
        mobile: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '修改用户信息' : '新增用户'
    }
  },
  created() {
    this.fetchData()
    this.fetchRoles()
  },
  methods: {
    // 获取列表
    fetchData() {
      this.loading = true
      request.get('/users/page', {
        params: {
          page: this.pagination.page,
          limit: this.pagination.limit,
          ...this.queryParams
        }
      }).then(res => {
        this.tableData = res.data.list
        this.total = res.data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 获取角色下拉框
    fetchRoles() {
      request.get('/role/list').then(res => {
        this.roleOptions = res.data
      })
    },
    handleSearch() {
      this.pagination.page = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = { username: '', role: '' }
      this.handleSearch()
    },
    handleSelectionChange(val) {
      this.selection = val
    },
    handleSizeChange(val) {
      this.pagination.limit = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.pagination.page = val
      this.fetchData()
    },
    // 新增
    handleAdd() {
      this.isEdit = false
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    // 编辑
    handleEdit(row) {
      this.isEdit = true
      this.dialogVisible = true
      // 复制数据，避免直接修改 tableData
      this.form = { ...row, password: '' } 
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    // 提交表单
    submitForm() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          const url = this.isEdit ? '/users/update' : '/users/save'
          // 注意：save/update 在 Controller 中是 @RequestBody，axios 默认就是 JSON
          request.post(url, this.form).then(() => {
            this.$message.success(this.isEdit ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.fetchData()
          })
        }
      })
    },
    // 单个删除
    handleDelete(row) {
      this.$confirm(`确认删除用户 "${row.username}" 吗?`, '警告', {
        type: 'warning'
      }).then(() => {
        request.post('/users/delete', [row.id]).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    // 批量删除
    handleBatchDelete() {
      const ids = this.selection.map(item => item.id)
      this.$confirm(`确认删除选中的 ${ids.length} 个用户吗?`, '警告', {
        type: 'warning'
      }).then(() => {
        request.post('/users/delete', ids).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    // 重置密码
    handleResetPwd(row) {
      this.$confirm(`确认重置用户 "${row.username}" 的密码为 123456 吗?`, '提示', {
        type: 'warning'
      }).then(() => {
        request.get('/users/resetPass', { params: { username: row.username } }).then(res => {
          this.$message.success(res.msg || '重置成功')
        })
      })
    },
    resetDialog() {
      this.form = {
        id: undefined,
        username: '',
        password: '',
        role: '',
        mobile: ''
      }
    }
  }
}
</script>

<style scoped>
.breadcrumb { margin-bottom: 20px; }
.toolbar { margin-bottom: 20px; }
</style>
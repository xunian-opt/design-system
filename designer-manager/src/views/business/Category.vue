<template>
  <div class="app-container">
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form" size="small">
        <el-form-item label="分类名称">
          <el-input v-model="queryParams.shangpinfenlei" placeholder="请输入分类名称" clearable @clear="handleSearch"></el-input>
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
          <template slot-scope="scope">
            {{ (pagination.page - 1) * pagination.limit + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="shangpinfenlei" label="分类名称" align="center"></el-table-column>
        <el-table-column label="操作" width="250" align="center" fixed="right">
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="resetForm" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px" :disabled="mode === 'view'" size="small">
        <el-form-item label="分类名称" prop="shangpinfenlei">
          <el-input v-model="form.shangpinfenlei" placeholder="请输入分类名称"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" v-if="mode !== 'view'">
        <el-button @click="dialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="submitForm" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'Category',
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
        shangpinfenlei: ''
      },
      dialogVisible: false,
      mode: 'add',
      form: {
        id: undefined,
        shangpinfenlei: ''
      },
      rules: {
        shangpinfenlei: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      const map = { add: '新增分类', edit: '修改分类', view: '分类详情' }
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
        limit: this.pagination.limit
      }
      if (this.queryParams.shangpinfenlei) {
        params.shangpinfenlei = this.queryParams.shangpinfenlei
      }

      request.get('/shangpinfenlei/page', { params }).then(res => {
        this.tableData = res.data.list
        this.total = res.data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleSearch() {
      this.pagination.page = 1
      this.fetchData()
    },
    resetSearch() {
      this.queryParams = { shangpinfenlei: '' }
      this.handleSearch()
    },
    handleSelectionChange(val) {
      this.selection = val
    },
    initForm(row, mode) {
      this.mode = mode
      this.dialogVisible = true
      if (mode === 'add') {
        this.form = { id: undefined, shangpinfenlei: '' }
      } else {
        this.form = { ...row }
      }
      this.$nextTick(() => {
        if (this.$refs.form && mode !== 'view') {
           this.$refs.form.clearValidate();
        }
      })
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
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const url = this.mode === 'edit' ? '/shangpinfenlei/update' : '/shangpinfenlei/save'
          request.post(url, this.form).then(() => {
            this.$message.success(this.mode === 'edit' ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.fetchData()
          })
        }
      })
    },
    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    handleDelete(id) {
      this.$confirm('确认删除该分类?', '提示', { type: 'warning' }).then(() => {
        request.post('/shangpinfenlei/delete', [id]).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    handleBatchDelete() {
      const ids = this.selection.map(item => item.id)
      this.$confirm(`确认删除选中的 ${ids.length} 个分类?`, '提示', { type: 'warning' }).then(() => {
        request.post('/shangpinfenlei/delete', ids).then(() => {
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
</style>
<template>
  <div class="app-container">
    <!-- <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
      <el-breadcrumb-item>轮播图管理</el-breadcrumb-item>
    </el-breadcrumb> -->

    <el-card>
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="名称">
          <el-input v-model="queryParams.name" placeholder="请输入名称" clearable @clear="handleSearch"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <div class="toolbar">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="el-icon-delete" :disabled="selection.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column type="index" label="索引" width="80" align="center">
           <template slot-scope="scope">{{ (pagination.page - 1) * pagination.limit + scope.$index + 1 }}</template>
        </el-table-column>
        <el-table-column prop="name" label="名称" align="center"></el-table-column>
        <el-table-column label="图片" align="center">
          <template slot-scope="scope">
             <el-image 
              v-if="scope.row.value"
              style="width: 120px; height: 60px" 
              :src="scope.row.value"
              :preview-src-list="[scope.row.value]">
            </el-image>
            <span v-else>无图</span>
          </template>
        </el-table-column>
        <el-table-column prop="value" label="图片地址/值" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="图片URL" prop="value">
          <el-input v-model="form.value" placeholder="请输入图片地址或上传"></el-input>
          </el-form-item>
         <el-form-item label="跳转链接">
          <el-input v-model="form.url" placeholder="可选"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
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
      pagination: { page: 1, limit: 10 },
      queryParams: { name: '' },
      dialogVisible: false,
      mode: 'add',
      form: { id: '', name: '', value: '', url: '' },
      rules: {
        name: [{ required: true, message: '必填', trigger: 'blur' }],
        value: [{ required: true, message: '必填', trigger: 'blur' }]
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
      request.get('/config/page', {
        params: { page: this.pagination.page, limit: this.pagination.limit, ...this.queryParams }
      }).then(res => {
        this.tableData = res.data.list
        this.total = res.data.total
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    handleSearch() { this.pagination.page = 1; this.fetchData() },
    resetSearch() { this.queryParams.name = ''; this.handleSearch() },
    handleSelectionChange(val) { this.selection = val },
    handleAdd() {
      this.mode = 'add'
      this.form = { name: '', value: '', url: '' }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.mode = 'edit'
      this.form = { ...row }
      this.dialogVisible = true
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const url = this.mode === 'edit' ? '/config/update' : '/config/save'
          request.post(url, this.form).then(() => {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.fetchData()
          })
        }
      })
    },
    handleDelete(id) {
       this.$confirm('确认删除?', '提示', { type: 'warning' }).then(() => {
         request.post('/config/delete', [id]).then(() => {
           this.$message.success('删除成功'); this.fetchData()
         })
       })
    },
    handleBatchDelete() {
      const ids = this.selection.map(i => i.id)
      this.$confirm(`确认删除 ${ids.length} 项?`, '提示', { type: 'warning' }).then(() => {
         request.post('/config/delete', ids).then(() => {
           this.$message.success('删除成功'); this.fetchData()
         })
       })
    }
  }
}
</script>
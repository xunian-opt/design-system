<template>
  <div class="app-container">
    <!-- <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item>业务管理</el-breadcrumb-item>
      <el-breadcrumb-item>订单管理</el-breadcrumb-item>
    </el-breadcrumb> -->

    <el-card>
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="订单编号">
          <el-input v-model="queryParams.orderid" placeholder="请输入订单号" clearable @clear="handleSearch"></el-input>
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="queryParams.goodname" placeholder="请输入商品名称" clearable @clear="handleSearch"></el-input>
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

      <el-table v-loading="loading" :data="tableData" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column type="index" label="索引" width="80" align="center">
          <template slot-scope="scope">
            {{ (pagination.page - 1) * pagination.limit + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="orderid" label="订单号" align="center"></el-table-column>
        <el-table-column prop="goodname" label="商品名称" align="center"></el-table-column>
        <el-table-column prop="buynumber" label="数量" align="center"></el-table-column>
        <el-table-column prop="total" label="总价" align="center">
           <template slot-scope="scope">¥ {{ scope.row.total }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '已支付' ? 'success' : 'warning'">{{ scope.row.status }}</el-tag>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="resetForm">
      <el-form :model="form" ref="form" label-width="80px" :disabled="mode === 'view'">
        <el-form-item label="订单号" prop="orderid">
          <el-input v-model="form.orderid" placeholder="请输入订单号"></el-input>
        </el-form-item>
        <el-form-item label="商品名称" prop="goodname">
          <el-input v-model="form.goodname" placeholder="请输入商品名称"></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="buynumber">
          <el-input-number v-model="form.buynumber" :min="1" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="总价" prop="total">
          <el-input-number v-model="form.total" :min="0" :precision="2" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="已支付" value="已支付"></el-option>
            <el-option label="已发货" value="已发货"></el-option>
            <el-option label="待支付" value="待支付"></el-option>
          </el-select>
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
  data() {
    return {
      loading: false,
      tableData: [],
      selection: [],
      total: 0,
      pagination: { page: 1, limit: 10 },
      queryParams: { orderid: '', goodname: '' },
      dialogVisible: false,
      mode: 'add', // add, edit, view
      form: { id: '', orderid: '', goodname: '', buynumber: 1, total: 0, status: '' }
    }
  },
  computed: {
    dialogTitle() {
      const map = { add: '新增订单', edit: '修改订单', view: '订单详情' }
      return map[this.mode]
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
	  //获取列表数据
    fetchData() {
          this.loading = true
          
          // 1. 定义基础分页参数
          const params = {
            page: this.pagination.page,
            limit: this.pagination.limit
          }
    
          // 2. 动态添加查询参数（只有当输入框有值时才传给后端）
          if (this.queryParams.orderid) {
            params.orderid = this.queryParams.orderid
          }
          if (this.queryParams.goodname) {
            params.goodname = this.queryParams.goodname
          }
    
          // 3. 发送请求
          request.get('/orders/page', {
            params: params
          }).then(res => {
            // 兼容后端返回结构
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
    handleSearch() {
      this.pagination.page = 1
      this.fetchData()
    },
    resetSearch() {
      this.queryParams = { orderid: '', goodname: '' }
      this.handleSearch()
    },
    handleSelectionChange(val) { this.selection = val },
    handleAdd() {
      this.mode = 'add'
      this.form = { orderid: '', goodname: '', buynumber: 1, total: 0, status: '' }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.mode = 'edit'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDetail(row) {
      this.mode = 'view'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(id) {
      this.$confirm('确认删除该订单?', '警告', { type: 'warning' }).then(() => {
        request.post('/orders/delete', [id]).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    handleBatchDelete() {
      const ids = this.selection.map(i => i.id)
      this.$confirm(`确认删除选中的 ${ids.length} 个订单?`, '警告', { type: 'warning' }).then(() => {
        request.post('/orders/delete', ids).then(() => {
          this.$message.success('批量删除成功')
          this.fetchData()
        })
      })
    },
    submitForm() {
      const url = this.mode === 'edit' ? '/orders/update' : '/orders/save'
      request.post(url, this.form).then(() => {
        this.$message.success('保存成功')
        this.dialogVisible = false
        this.fetchData()
      })
    },
    resetForm() {
      if (this.$refs.form) this.$refs.form.resetFields()
    }
  }
}
</script>

<style scoped>
.breadcrumb { margin-bottom: 20px; }
.toolbar { margin-bottom: 20px; }
.search-form { margin-bottom: 10px; }
</style>
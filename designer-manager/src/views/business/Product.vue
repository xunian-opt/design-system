<template>
  <div class="app-container">
    <!-- <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item>业务管理</el-breadcrumb-item>
      <el-breadcrumb-item>装修商品管理</el-breadcrumb-item>
    </el-breadcrumb> -->

    <el-card class="box-card">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="商品名称">
          <el-input v-model="queryParams.shangpinmingcheng" placeholder="请输入名称" clearable @clear="handleSearch"></el-input>
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

      <el-table v-loading="loading" :data="tableData" border @selection-change="handleSelectionChange" style="width: 100%">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column type="index" label="索引" width="80" align="center">
           <template slot-scope="scope">{{ (pagination.page - 1) * pagination.limit + scope.$index + 1 }}</template>
        </el-table-column>
        <el-table-column prop="shangpinmingcheng" label="商品名称" align="center"></el-table-column>
        <el-table-column prop="shangpinpinpai" label="品牌" align="center"></el-table-column>
        <el-table-column prop="shangpinfenlei" label="分类名称" align="center"></el-table-column>
        <el-table-column prop="price" label="价格" align="center"></el-table-column>
        <el-table-column label="图片" align="center">
          <template slot-scope="scope">
            <el-image 
              v-if="scope.row.shangpintupian"
              style="width: 50px; height: 50px; border-radius: 4px;" 
              :src="getFirstImg(scope.row.shangpintupian)" 
              :preview-src-list="getImgList(scope.row.shangpintupian)">
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px" :disabled="mode === 'view'">
        <el-form-item label="商品名称" prop="shangpinmingcheng">
          <el-input v-model="form.shangpinmingcheng"></el-input>
        </el-form-item>
        <el-form-item label="品牌" prop="shangpinpinpai">
          <el-input v-model="form.shangpinpinpai"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="shangpinfenlei">
          <el-select v-model="form.shangpinfenlei" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="item in categoryOptions" :key="item.id" :label="item.shangpinfenlei" :value="item.shangpinfenlei"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="商品图片">
          <el-upload
            :action="uploadUrl"
            :headers="headers"
            list-type="picture-card"
            :file-list="fileList"
            :on-success="handleUploadSuccess"
            :on-remove="handleRemove"
            :disabled="mode === 'view'"
            :class="{ 'hide-upload': mode === 'view' }"
          >
            <i class="el-icon-plus"></i>
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
  data() {
    return {
      loading: false,
      tableData: [],
      selection: [],
      total: 0,
      pagination: { page: 1, limit: 10 },
      queryParams: { shangpinmingcheng: '' },
      dialogVisible: false,
      mode: 'add',
      categoryOptions: [],
      form: { id: '', shangpinmingcheng: '', shangpinpinpai: '', shangpinfenlei: '', price: 0, shangpintupian: '' },
      fileList: [],
      uploadUrl: '/springboot2z04j/file/upload',
      headers: { Token: localStorage.getItem('token') },
      rules: {
        shangpinmingcheng: [{ required: true, message: '必填', trigger: 'blur' }],
        shangpinpinpai: [{ required: true, message: '必填', trigger: 'blur' }]
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
      request.get('/zhuangxiushangpin/page', {
        params: { page: this.pagination.page, limit: this.pagination.limit, ...this.queryParams }
      }).then(res => {
        this.tableData = res.data.list
        this.total = res.data.total
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    fetchCategories() {
      request.get('/shangpinfenlei/lists').then(res => { this.categoryOptions = res.data })
    },
    getFirstImg(imgs) {
      if (!imgs) return ''
      return '/springboot2z04j/upload/' + imgs.split(',')[0]
    },
    getImgList(imgs) {
      if (!imgs) return []
      return imgs.split(',').map(f => '/springboot2z04j/upload/' + f)
    },
    handleSearch() { this.pagination.page = 1; this.fetchData() },
    resetSearch() { this.queryParams.shangpinmingcheng = ''; this.handleSearch() },
    handleSelectionChange(val) { this.selection = val },
    
    initForm(row, mode) {
      this.mode = mode
      this.dialogVisible = true
      if (mode === 'add') {
        this.form = { shangpinmingcheng: '', shangpinpinpai: '', shangpinfenlei: '', price: 0, shangpintupian: '' }
        this.fileList = []
      } else {
        this.form = { ...row }
        this.fileList = row.shangpintupian ? row.shangpintupian.split(',').map(f => ({
          name: f,
          url: '/springboot2z04j/upload/' + f
        })) : []
      }
    },
    handleAdd() { this.initForm(null, 'add') },
    handleEdit(row) { this.initForm(row, 'edit') },
    handleDetail(row) { this.initForm(row, 'view') },
    
    handleUploadSuccess(res, file) {
      if (res.code === 0) {
        file.name = res.file
        file.url = '/springboot2z04j/upload/' + res.file
        this.fileList.push(file)
      } else {
        this.$message.error('上传失败')
      }
    },
    handleRemove(file, fileList) {
      this.fileList = fileList
    },
    
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.form.shangpintupian = this.fileList.map(f => f.name).join(',')
          const url = this.mode === 'edit' ? '/zhuangxiushangpin/update' : '/zhuangxiushangpin/save'
          request.post(url, this.form).then(() => {
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.fetchData()
          })
        }
      })
    },
    handleDelete(id) {
       this.$confirm('确认删除?', '提示', { type: 'warning' }).then(() => {
         request.post('/zhuangxiushangpin/delete', [id]).then(() => {
           this.$message.success('删除成功'); this.fetchData()
         })
       })
    },
    handleBatchDelete() {
      const ids = this.selection.map(i => i.id)
      this.$confirm(`确认删除 ${ids.length} 项?`, '提示', { type: 'warning' }).then(() => {
         request.post('/zhuangxiushangpin/delete', ids).then(() => {
           this.$message.success('删除成功'); this.fetchData()
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
.hide-upload .el-upload--picture-card { display: none; }
</style>
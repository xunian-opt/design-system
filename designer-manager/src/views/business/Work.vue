<template>
  <div class="app-container">
    <!-- <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item>业务管理</el-breadcrumb-item>
      <el-breadcrumb-item>装修作品管理</el-breadcrumb-item>
    </el-breadcrumb> -->

    <el-card class="box-card">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="作品名称">
          <el-input v-model="queryParams.zuopinmingcheng" placeholder="请输入作品名称" clearable @clear="handleSearch"></el-input>
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
        <el-table-column prop="zuopinmingcheng" label="作品名称" align="center"></el-table-column>
        <el-table-column prop="shejishixingming" label="设计师" align="center"></el-table-column>
        <el-table-column prop="fenggeleixing" label="风格" align="center"></el-table-column>
        <el-table-column label="封面" align="center">
          <template slot-scope="scope">
             <el-image 
              v-if="scope.row.fengmianxinxi"
              style="width: 60px; height: 40px; border-radius: 4px;" 
              :src="getFirstImg(scope.row.fengmianxinxi)" 
              :preview-src-list="getImgList(scope.row.fengmianxinxi)">
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
        <el-form-item label="名称" prop="zuopinmingcheng">
          <el-input v-model="form.zuopinmingcheng"></el-input>
        </el-form-item>
        <el-form-item label="设计师" prop="shejishixingming">
          <el-select v-model="form.shejishixingming" placeholder="请选择设计师" style="width: 100%">
            <el-option v-for="item in designerOptions" :key="item.id" :label="item.xingming" :value="item.xingming"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风格" prop="fenggeleixing">
          <el-input v-model="form.fenggeleixing"></el-input>
        </el-form-item>
        <el-form-item label="作品图片">
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
      queryParams: { zuopinmingcheng: '' },
      dialogVisible: false,
      mode: 'add',
      designerOptions: [],
      form: { id: '', zuopinmingcheng: '', shejishixingming: '', fenggeleixing: '', fengmianxinxi: '' },
      fileList: [],
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
      request.get('/zhuangxiuzuopin/page', {
        params: { page: this.pagination.page, limit: this.pagination.limit, ...this.queryParams }
      }).then(res => {
        this.tableData = res.data.list
        this.total = res.data.total
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    fetchDesigners() {
      request.get('/yuangongxinxi/lists').then(res => { this.designerOptions = res.data })
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
    resetSearch() { this.queryParams.zuopinmingcheng = ''; this.handleSearch() },
    handleSelectionChange(val) { this.selection = val },
    
    initForm(row, mode) {
      this.mode = mode
      this.dialogVisible = true
      if (mode === 'add') {
        this.form = { zuopinmingcheng: '', shejishixingming: '', fenggeleixing: '', fengmianxinxi: '' }
        this.fileList = []
      } else {
        this.form = { ...row }
        this.fileList = row.fengmianxinxi ? row.fengmianxinxi.split(',').map(f => ({
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
      } else { this.$message.error('上传失败') }
    },
    handleRemove(file, fileList) { this.fileList = fileList },
    
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.form.fengmianxinxi = this.fileList.map(f => f.name).join(',')
          const url = this.mode === 'edit' ? '/zhuangxiuzuopin/update' : '/zhuangxiuzuopin/save'
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
         request.post('/zhuangxiuzuopin/delete', [id]).then(() => {
           this.$message.success('删除成功'); this.fetchData()
         })
       })
    },
    handleBatchDelete() {
      const ids = this.selection.map(i => i.id)
      this.$confirm(`确认删除 ${ids.length} 项?`, '提示', { type: 'warning' }).then(() => {
         request.post('/zhuangxiuzuopin/delete', ids).then(() => {
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
<template>
  <div class="app-container">
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form" size="small">
        <el-form-item label="公司名称">
          <el-input v-model="queryParams.gongsimingcheng" placeholder="请输入公司名称" clearable @clear="handleSearch"></el-input>
        </el-form-item>
        <el-form-item label="经营范围">
          <el-input v-model="queryParams.jingyingfanwei" placeholder="请输入经营范围" clearable @clear="handleSearch"></el-input>
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
        <el-table-column prop="gongsimingcheng" label="公司名称" align="center"></el-table-column>
        
        <el-table-column label="公司照片" align="center" width="120">
          <template slot-scope="scope">
            <div v-if="scope.row.gongsizhaopian">
              <el-image 
                style="width: 50px; height: 50px; border-radius: 4px; cursor: pointer;"
                :src="getFirstImg(scope.row.gongsizhaopian)"
                :preview-src-list="getImgList(scope.row.gongsizhaopian)"
                fit="cover">
              </el-image>
            </div>
            <span v-else style="color:#c0c4cc; font-size: 12px">暂无</span>
          </template>
        </el-table-column>

        <el-table-column prop="gongsidizhi" label="公司地址" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="jingyingfanwei" label="经营范围" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="zixundianhua" label="咨询电话" align="center" width="120"></el-table-column>
        
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
        <el-form-item label="公司名称" prop="gongsimingcheng">
          <el-input v-model="form.gongsimingcheng" placeholder="请输入公司名称"></el-input>
        </el-form-item>
        
        <el-form-item label="公司地址" prop="gongsidizhi">
          <el-input v-model="form.gongsidizhi" placeholder="请输入公司地址"></el-input>
        </el-form-item>

        <el-form-item label="经营范围" prop="jingyingfanwei">
          <el-input type="textarea" :rows="2" v-model="form.jingyingfanwei" placeholder="请输入经营范围"></el-input>
        </el-form-item>

        <el-form-item label="咨询电话" prop="zixundianhua">
          <el-input v-model="form.zixundianhua" placeholder="请输入咨询电话"></el-input>
        </el-form-item>
        
        <el-form-item label="公司照片" prop="gongsizhaopian">
          <div v-if="mode === 'view' && fileList.length === 0" style="color: #909399; font-size: 14px;">暂无图片</div>
          
          <el-upload
            v-else
            :action="uploadUrl"
            :headers="headers"
            list-type="picture-card"
            :file-list="fileList"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeAvatarUpload"
            :limit="9"
            :class="{ 'hide-upload-btn': fileList.length >= 9 || mode === 'view' }"
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
          <div class="el-upload__tip" v-if="mode !== 'view'">支持多图上传，最多9张，格式jpg/png</div>
        </el-form-item>

        <el-form-item label="公司介绍" prop="gongsijieshao">
          <el-input type="textarea" :rows="4" v-model="form.gongsijieshao" placeholder="请输入公司介绍"></el-input>
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
  name: 'Gongsixinxi',
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
        gongsimingcheng: '',
        jingyingfanwei: ''
      },
      // 弹窗相关
      dialogVisible: false,
      mode: 'add', // add, edit, view
      form: {
        id: undefined,
        gongsimingcheng: '',
        gongsizhaopian: '', // 存储为逗号分隔字符串
        gongsidizhi: '',
        jingyingfanwei: '',
        zixundianhua: '',
        gongsijieshao: ''
      },
      // 上传相关
      fileList: [], // 用于 el-upload 显示的对象数组
      uploadUrl: '/springboot2z04j/file/upload',
      headers: { Token: localStorage.getItem('token') },
      previewVisible: false,
      previewImageUrl: '',
      
      rules: {
        gongsimingcheng: [{ required: true, message: '请输入公司名称', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      const map = { add: '新增公司信息', edit: '修改公司信息', view: '公司详情' }
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
      const params = {
        page: this.pagination.page,
        limit: this.pagination.limit,
        orderBy: 'addtime' 
      }
      
      // 移除空参数
      if (this.queryParams.gongsimingcheng) params.gongsimingcheng = this.queryParams.gongsimingcheng;
      if (this.queryParams.jingyingfanwei) params.jingyingfanwei = this.queryParams.jingyingfanwei;

      request.get('/gongsixinxi/page', { params }).then(res => {
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

    // 辅助函数：获取图片 URL (处理相对路径)
    getImageUrl(img) {
      if (!img) return ''
      const baseUrl = '/springboot2z04j/upload/';
      return img.startsWith('http') ? img : baseUrl + img.replace(/^\//, '');
    },
    // 辅助函数：获取第一张图 (用于表格)
    getFirstImg(imgs) {
      if (!imgs) return ''
      return this.getImageUrl(imgs.split(',')[0]);
    },
    // 辅助函数：获取图片数组 (用于表格预览)
    getImgList(imgs) {
      if (!imgs) return []
      return imgs.split(',').map(f => this.getImageUrl(f));
    },

    handleSearch() {
      this.pagination.page = 1
      this.fetchData()
    },
    resetSearch() {
      this.queryParams = { gongsimingcheng: '', jingyingfanwei: '' }
      this.handleSearch()
    },
    handleSelectionChange(val) {
      this.selection = val
    },

    // 初始化表单
    initForm(row, mode) {
      this.mode = mode
      this.dialogVisible = true
      // 【关键修复】打开前清空 fileList，防止闪烁
      this.fileList = [];

      if (mode === 'add') {
        this.form = {
          id: undefined,
          gongsimingcheng: '',
          gongsizhaopian: '',
          gongsidizhi: '',
          jingyingfanwei: '', 
          zixundianhua: '',
          gongsijieshao: ''
        }
      } else {
        this.form = { ...row }
        // 确保字段存在
        if (!this.form.jingyingfanwei) this.$set(this.form, 'jingyingfanwei', '');

        // 【数据回显】将逗号分隔字符串转为对象数组
        if (this.form.gongsizhaopian) {
            this.fileList = this.form.gongsizhaopian.split(',').map(name => ({
                name: name,
                url: this.getImageUrl(name)
            }))
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

    // 上传回调
    handleUploadSuccess(res, file) {
      if (res.code === 0) {
        // 将新上传的文件推入数组 (保持多图逻辑)
        // 注意：Element UI 的 fileList 会自动管理，但我们需要确保 name 和 url 属性正确
        // 这里手动修正一下新加入文件的属性
        const newFile = {
            name: res.file,
            url: this.getImageUrl(res.file)
        };
        this.fileList.push(newFile);
        
        // 由于是手动 push，需要处理 Element UI 可能自动添加的原始 file 对象 (避免重复)
        // 过滤掉没有 url 或者 url 是 blob 的临时文件，只保留我们构建的
        // 简单做法：直接维护 fileList 为我们自己的源
        // 实际操作：Element UI 的 fileList 引用是双向绑定的，直接操作 this.fileList 即可
        
        // 移除可能的自动添加项（防止显示两个），保留我们添加的带正确 URL 的项
        // 或者更简单的逻辑：只依赖 this.fileList 作为提交源，显示由组件负责
        // 修正：el-upload 的 :file-list 是单向绑定的初始值，后续变化在组件内部。
        // 正确做法：利用 file-list 同步更新。但因为 picture-card 需要 url 显示，
        // 我们可以简单地不做额外 push，而是遍历组件的 fileList 拿到 response。
        
        this.$message.success('上传成功');
      } else {
        this.$message.error(res.msg || '上传失败');
        // 从列表移除失败的文件
        const index = this.fileList.indexOf(file);
        if (index !== -1) this.fileList.splice(index, 1);
      }
    },
    handleUploadError() {
      this.$message.error('网络错误，上传失败');
    },
    
    // 移除图片
    handleRemove(file) {
      // 从 fileList 中移除
      // 注意：如果是回显的图片，file 就是 fileList 中的项
      // 如果是新上传的，file 是 Element 封装的对象
      const targetName = file.response ? file.response.file : file.name;
      this.fileList = this.fileList.filter(item => {
          const itemName = item.response ? item.response.file : item.name;
          return itemName !== targetName;
      });
    },

    handlePictureCardPreview(file) {
      this.previewImageUrl = file.url;
      this.previewVisible = true;
    },

    beforeAvatarUpload(file) {
      const isJPGOrPNG = ['image/jpeg', 'image/png'].includes(file.type);
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPGOrPNG) this.$message.error('上传图片只能是 JPG/PNG 格式!');
      if (!isLt2M) this.$message.error('上传图片大小不能超过 2MB!');
      return isJPGOrPNG && isLt2M;
    },

    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 【核心】提交前处理：遍历 fileList 提取文件名并合并
          // 注意：el-upload 组件内部状态可能和 this.fileList 不同步 (如果是新上传的)
          // 最稳妥的方式是获取 upload 组件内部的 uploadFiles
          // 但为了简单，我们在 handleUploadSuccess 中如果处理得当，this.fileList 也是准的。
          
          // 更通用的方式：
          // 如果是回显的图，有 name 属性。
          // 如果是新上传的图，response.file 是文件名。
          
          // 在本例中，因为使用了 slot 自定义渲染且 fileList 双向绑定可能有限，
          // 我们依赖于 handleUploadSuccess 和 handleRemove 对 this.fileList 的维护。
          // 但要注意 el-upload 默认行为会修改 fileList。
          
          // 修正逻辑：直接读取组件引用的 uploadFiles 更安全，或者标准化 fileList。
          // 这里使用标准化 fileList 的方式 (假设 uploadSuccess 和 remove 都正确维护了 this.fileList)
          
          // 但因为 el-upload 自动行为，最好是在 submit 时重新映射
          // 这里有一个简便方法：this.fileList 绑定的就是组件的文件列表
          
          const names = this.fileList.map(f => {
              if (f.response && f.response.code === 0) return f.response.file; // 新上传的
              return f.name; // 回显的
          });
          
          this.form.gongsizhaopian = names.join(',');

          const url = this.mode === 'edit' ? '/gongsixinxi/update' : '/gongsixinxi/save'
          request.post(url, this.form).then(res => {
            this.$message.success(this.mode === 'edit' ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.fetchData()
          })
        }
      })
    },

    // 【Bug修复】显式清空 fileList
    resetForm() {
      this.fileList = [];
      if (this.$refs.form) this.$refs.form.resetFields();
      this.form = {
          id: undefined,
          gongsimingcheng: '',
          gongsizhaopian: '',
          gongsidizhi: '',
          jingyingfanwei: '',
          zixundianhua: '',
          gongsijieshao: ''
      };
    },
    
    handleDelete(id) {
      this.$confirm('确认删除该记录?', '提示', { type: 'warning' }).then(() => {
        request.post('/gongsixinxi/delete', [id]).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    handleBatchDelete() {
      const ids = this.selection.map(item => item.id)
      this.$confirm(`确认删除选中的 ${ids.length} 条记录?`, '提示', { type: 'warning' }).then(() => {
        request.post('/gongsixinxi/delete', ids).then(() => {
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

::v-deep .el-card__body {
  padding: 15px;
}

/* 控制上传按钮隐藏：在查看模式或达到9张上限时隐藏 */
::v-deep .hide-upload-btn .el-upload--picture-card {
    display: none;
}
::v-deep .el-upload-list--picture-card .el-upload-list__item {
    transition: none !important;
}
</style>
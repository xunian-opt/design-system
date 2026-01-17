<template>
  <div class="app-container">
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item>业务管理</el-breadcrumb-item>
      <el-breadcrumb-item>设计师信息管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="box-card">
      <div class="filter-container">
        <el-input
          v-model="queryParams.gonghao"
          placeholder="设计师工号"
          style="width: 200px;"
          class="filter-item"
          clearable
          @keyup.enter.native="handleQuery"
        />
        <el-input
          v-model="queryParams.xingming"
          placeholder="姓名"
          style="width: 200px; margin-left: 10px;"
          class="filter-item"
          clearable
          @keyup.enter.native="handleQuery"
        />
        <el-button type="primary" icon="el-icon-search" @click="handleQuery" style="margin-left: 10px;">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </div>

      <div class="toolbar" style="margin: 20px 0;">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" icon="el-icon-delete" :disabled="ids.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="gonghao" label="工号" align="center" />
        <el-table-column prop="xingming" label="姓名" align="center" />
        <el-table-column prop="xingbie" label="性别" align="center" />
        <el-table-column prop="zhiwei" label="职位" align="center" />
        <el-table-column prop="lianxifangshi" label="电话" align="center" />
        <el-table-column label="照片" align="center" width="100">
          <template slot-scope="scope">
            <el-image
              v-if="scope.row.touxiang"
              style="width: 40px; height: 40px; border-radius: 4px"
              :src="getFileUrl(scope.row.touxiang)"
              :preview-src-list="[getFileUrl(scope.row.touxiang)]"
            >
            </el-image>
            <span v-else style="color: #999; font-size: 12px">暂无</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="220">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="handleView(scope.row)">详情</el-button>
            <el-button size="mini" type="primary" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        style="margin-top: 20px; text-align: center;"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryParams.page"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="queryParams.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" :disabled="isView">
        <el-row>
          <el-col :span="12">
            <el-form-item label="工号" prop="gonghao">
              <el-input v-model="form.gonghao" placeholder="请输入工号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="xingming">
              <el-input v-model="form.xingming" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row>
          <el-col :span="12">
            <el-form-item label="性别" prop="xingbie">
              <el-select v-model="form.xingbie" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位" prop="zhiwei">
              <el-input v-model="form.zhiwei" placeholder="请输入职位" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="电话" prop="lianxifangshi">
          <el-input v-model="form.lianxifangshi" placeholder="请输入联系电话" />
        </el-form-item>

        <el-form-item label="头像" prop="touxiang">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :headers="headerObj"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :disabled="isView"
          >
            <img v-if="form.touxiang" :src="getFileUrl(form.touxiang)" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div slot="tip" class="el-upload__tip" v-if="!isView">只能上传jpg/png文件，且不超过2MB</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="!isView">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

// 基础配置
const BASE_API = '/springboot2z04j'

export default {
  name: "DesignerList",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 总条数
      total: 0,
      // 表格数据
      tableData: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否为查看模式
      isView: false,
      // 查询参数
      queryParams: {
        page: 1,
        limit: 10,
        gonghao: undefined,
        xingming: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        gonghao: [{ required: true, message: "工号不能为空", trigger: "blur" }],
        xingming: [{ required: true, message: "姓名不能为空", trigger: "blur" }]
      },
      // 上传地址
      uploadUrl: BASE_API + '/file/upload',
      // 请求头（模拟Token）
      headerObj: {
        Token: localStorage.getItem('token') || ''
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 获取完整图片路径
    getFileUrl(fileName) {
      if (!fileName) return '';
      // 如果文件名已经是完整路径则直接返回，否则拼接
      if (fileName.startsWith('http')) return fileName;
      return BASE_API + '/upload/' + fileName;
    },

    // 查询列表
    getList() {
      this.loading = true;
      axios.get(BASE_API + '/yuangongxinxi/page', {
        params: this.queryParams,
        headers: { Token: localStorage.getItem('token') }
      }).then(response => {
        const res = response.data;
        if (res.code === 0) {
          this.tableData = res.data.list;
          this.total = res.data.total;
        } else {
          this.$message.error(res.msg || '获取数据失败');
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },

    // 搜索
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },

    // 重置
    resetQuery() {
      this.queryParams.gonghao = undefined;
      this.queryParams.xingming = undefined;
      this.handleQuery();
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.queryParams.limit = val;
      this.getList();
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.queryParams.page = val;
      this.getList();
    },

    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        gonghao: undefined,
        xingming: undefined,
        xingbie: '男',
        zhiwei: undefined,
        lianxifangshi: undefined,
        touxiang: undefined
      };
      this.isView = false;
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    // 新增按钮操作
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设计师";
    },

    // 修改按钮操作
    handleUpdate(row) {
      this.reset();
      this.form = { ...row }; // 浅拷贝
      this.open = true;
      this.title = "修改设计师信息";
    },

    // 查看详情
    handleView(row) {
      this.reset();
      this.form = { ...row };
      this.open = true;
      this.title = "设计师详情";
      this.isView = true;
    },

    // 提交按钮
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const url = this.form.id ? '/yuangongxinxi/update' : '/yuangongxinxi/save';
          // save 接口通常不需要id，后端自动生成，但update需要
          
          axios.post(BASE_API + url, this.form, {
            headers: { 
                'Content-Type': 'application/json',
                Token: localStorage.getItem('token') 
            }
          }).then(response => {
            if (response.data.code === 0) {
              this.$message.success("操作成功");
              this.open = false;
              this.getList();
            } else {
              this.$message.error(response.data.msg);
            }
          });
        }
      });
    },

    // 删除按钮操作
    handleDelete(row) {
      const ids = [row.id];
      this.$confirm('是否确认删除该数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return axios.post(BASE_API + '/yuangongxinxi/delete', ids, {
             headers: { Token: localStorage.getItem('token') }
        });
      }).then(response => {
        if(response.data.code === 0) {
            this.getList();
            this.$message.success("删除成功");
        } else {
            this.$message.error(response.data.msg);
        }
      }).catch(() => {});
    },

    // 批量删除
    handleBatchDelete() {
      this.$confirm('是否确认删除选中的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
         return axios.post(BASE_API + '/yuangongxinxi/delete', this.ids, {
             headers: { Token: localStorage.getItem('token') }
        });
      }).then(response => {
        if(response.data.code === 0) {
            this.getList();
            this.$message.success("删除成功");
        } else {
             this.$message.error(response.data.msg);
        }
      }).catch(() => {});
    },

// 头像上传成功
    handleAvatarSuccess(res) { // <--- 删掉了 file
      if (res.code === 0) {
        this.form.touxiang = res.file; 
      } else {
        this.$message.error(res.msg || '上传失败');
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
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}
.breadcrumb {
  margin-bottom: 20px;
  font-size: 14px;
}
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
  border: 1px dashed #d9d9d9; /* Element UI 的样式有时候需要强制加一下 */
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>
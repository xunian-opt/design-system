<template>
  <div class="app-container">
    <el-card class="table-card" shadow="never">
      <div class="toolbar">
        <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAdd">新增角色</el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%" size="medium">
        <el-table-column type="index" label="索引" width="80" align="center">
           <template slot-scope="scope">{{ (pagination.page - 1) * pagination.limit + scope.$index + 1 }}</template>
        </el-table-column>
        <el-table-column prop="name" label="角色名称" align="center"></el-table-column>
        <el-table-column prop="code" label="角色编码" align="center"></el-table-column>
        <el-table-column label="操作" width="300" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" class="blue-text" @click="handleEdit(scope.row)">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-circle-check" class="blue-text" @click="handleAuth(scope.row)">菜单权限</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" class="red-text" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        style="margin-top: 20px; text-align: right;"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.page"
        :page-sizes="[10, 20, 50]"
        :page-size="pagination.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px" size="small">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="例如：admin, user"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="submitForm" size="small">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="分配菜单权限" :visible.sync="authOpen" width="500px" :close-on-click-modal="false">
      <el-form label-width="80px" size="small">
        <el-form-item label="角色名称">
          <el-input v-model="authForm.name" disabled />
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event)">展开/折叠</el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event)">全选/全不选</el-checkbox>
          <el-tree
            class="tree-border"
            :data="menuOptions"
            show-checkbox
            ref="menu"
            node-key="id"
            :check-strictly="false"
            empty-text="加载中，请稍候"
            :props="{ label: 'title', children: 'children' }"
          ></el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="authOpen = false" size="small">取 消</el-button>
        <el-button type="primary" @click="submitAuth" size="small">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'
import { mockMenuData } from '@/utils/mockMenu'

export default {
  name: 'Roles',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      pagination: { page: 1, limit: 10 },
      dialogVisible: false,
      mode: 'add',
      form: { id: '', name: '', code: '' },
      rules: {
        name: [{ required: true, message: '必填', trigger: 'blur' }],
        code: [{ required: true, message: '必填', trigger: 'blur' }]
      },
      authOpen: false,
      menuExpand: false,
      menuNodeAll: false,
      menuOptions: [],
      authForm: { id: '', name: '' }
    }
  },
  computed: {
    dialogTitle() { return this.mode === 'add' ? '新增角色' : '修改角色' }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      request.get('/role/page', {
        params: { page: this.pagination.page, limit: this.pagination.limit }
      }).then(res => {
        if (res.code === 0) {
           const pageData = res.data
           if (pageData && pageData.list) {
              this.tableData = pageData.list
              this.total = pageData.totalCount || pageData.total || 0
           } else if (Array.isArray(pageData)) {
              this.tableData = pageData
              this.total = pageData.length
           } else {
              this.tableData = []
              this.total = 0
           }
        } else {
           this.tableData = [] 
        }
        this.loading = false
      }).catch(err => {
        console.error(err)
        this.loading = false
      })
    },
    handleAdd() {
      this.mode = 'add'
      this.form = { name: '', code: '' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    handleEdit(row) {
      this.mode = 'edit'
      this.form = { ...row } 
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const url = this.mode === 'edit' ? '/role/update' : '/role/save'
          request.post(url, this.form).then(res => {
            if (res.code === 0) {
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.fetchData()
            } else {
              this.$message.error(res.msg || '保存失败')
            }
          })
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确认删除该角色?', '提示', { type: 'warning' }).then(() => {
        request.post('/role/delete', [id]).then(res => {
          if (res.code === 0) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    handleAuth(row) {
      if (row.code === 'admin') {
        this.$message.warning('管理员拥有所有权限，无需分配');
        return;
      }
      this.authForm = { id: row.id, name: row.name };
      this.authOpen = true;
      this.menuExpand = false;
      this.menuNodeAll = false;
      this.menuOptions = JSON.parse(JSON.stringify(mockMenuData)); 
      
      request.get(`/role/menus/${row.id}`).then(res => {
        const checkedKeys = (res.code === 0 && res.data) ? res.data : [];
        this.$nextTick(() => {
            this.$refs.menu.setCheckedKeys(checkedKeys);
            this.handleCheckedTreeExpand(false); 
        });
      }).catch(() => {
        this.$nextTick(() => {
           this.$refs.menu.setCheckedKeys([]);
           this.handleCheckedTreeExpand(false);
        });
      })
    },
    handleCheckedTreeExpand(value) {
      let treeList = this.menuOptions;
      for (let i = 0; i < treeList.length; i++) {
        this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
      }
    },
    handleCheckedTreeNodeAll(value) {
      this.$refs.menu.setCheckedNodes(value ? this.menuOptions : []);
    },
    submitAuth() {
      const roleId = this.authForm.id;
      const menuIds = this.$refs.menu.getCheckedKeys();
      const halfMenuIds = this.$refs.menu.getHalfCheckedKeys();
      const finalMenuIds = [...menuIds, ...halfMenuIds];
      
      request.post('/role/menu/save', { 
        roleId: roleId, 
        menuIds: finalMenuIds 
      }).then(res => {
        if(res.code === 0) {
            this.$message.success("权限分配成功");
            this.authOpen = false;
        } else {
            this.$message.error(res.msg || "分配失败");
        }
      })
    },
    handleSizeChange(val) {
      this.pagination.limit = val;
      this.fetchData();
    },
    handleCurrentChange(val) {
      this.pagination.page = val;
      this.fetchData();
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
.table-card {
  border-radius: 4px;
  border: none;
  min-height: 500px;
}
.toolbar { margin-bottom: 15px; }
.blue-text { color: #1890ff; }
.red-text { color: #ff4d4f; }

::v-deep .el-card__body { padding: 15px; }

.tree-border {
  margin-top: 5px;
  border: 1px solid #e5e6e7;
  background: #FFFFFF none;
  border-radius: 4px;
  padding: 10px;
  max-height: 400px;
  overflow-y: auto;
}
</style>
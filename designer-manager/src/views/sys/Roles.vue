<template>
  <div class="app-container">
    <el-card class="box-card">
      <div class="toolbar">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增角色</el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column type="index" label="索引" width="80" align="center">
           <template slot-scope="scope">{{ (pagination.page - 1) * pagination.limit + scope.$index + 1 }}</template>
        </el-table-column>
        <el-table-column prop="name" label="角色名称" align="center"></el-table-column>
        <el-table-column prop="code" label="角色编码" align="center"></el-table-column>
        <el-table-column label="操作" width="300" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">修改</el-button>
            <el-button size="mini" type="success" icon="el-icon-circle-check" @click="handleAuth(scope.row)">菜单权限</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row.id)">删除</el-button>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="例如：admin, user"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="分配菜单权限" :visible.sync="authOpen" width="500px">
      <el-form label-width="80px">
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
        <el-button type="primary" @click="submitAuth">确 定</el-button>
        <el-button @click="authOpen = false">取 消</el-button>
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
      
      // 增删改数据
      dialogVisible: false,
      mode: 'add',
      form: { id: '', name: '', code: '' },
      rules: {
        name: [{ required: true, message: '必填', trigger: 'blur' }],
        code: [{ required: true, message: '必填', trigger: 'blur' }]
      },

      // 权限分配数据
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
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    handleEdit(row) {
      this.mode = 'edit'
      this.form = { ...row } 
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
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

    // --- 权限分配方法 (关键修改点) ---
    handleAuth(row) {
      if (row.code === 'admin') {
        this.$message.warning('管理员拥有所有权限，无需分配');
        return;
      }
      this.authForm = { id: row.id, name: row.name };
      this.authOpen = true;
      this.menuExpand = false; // 默认不展开
      this.menuNodeAll = false;
      
      this.menuOptions = JSON.parse(JSON.stringify(mockMenuData)); 
      
      request.get(`/role/menus/${row.id}`).then(res => {
        const checkedKeys = (res.code === 0 && res.data) ? res.data : [];
        this.$nextTick(() => {
            this.$refs.menu.setCheckedKeys(checkedKeys);
            // 修改点：调用已有的 handleCheckedTreeExpand 方法来折叠所有节点
            this.handleCheckedTreeExpand(false); 
        });
      }).catch(() => {
        this.$nextTick(() => {
           this.$refs.menu.setCheckedKeys([]);
           this.handleCheckedTreeExpand(false);
        });
      })
    },

    // 控制树的展开/折叠
    handleCheckedTreeExpand(value) {
      let treeList = this.menuOptions;
      for (let i = 0; i < treeList.length; i++) {
        // 通过 id 找到对应节点并设置 expanded 属性
        this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
      }
    },
    // 控制全选/全不选
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
.toolbar { margin-bottom: 20px; }
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
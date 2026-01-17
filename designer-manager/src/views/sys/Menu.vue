<template>
  <div class="app-container">
    <el-card>
      <div class="toolbar">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增菜单</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        row-key="id"
        border
        default-expand-all
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      >
        <el-table-column prop="title" label="菜单名称" min-width="150"></el-table-column>
        <el-table-column prop="icon" label="图标" align="center" width="80">
          <template slot-scope="scope">
            <i :class="scope.row.icon"></i>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" align="center"></el-table-column>
        <el-table-column prop="perms" label="权限标识" align="center"></el-table-column>
        <el-table-column prop="orderNum" label="排序" align="center" width="80"></el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">修改</el-button>
            <el-button size="mini" type="text" @click="handleAdd(scope.row)">新增下级</el-button>
            <el-button size="mini" type="text" class="text-danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="上级菜单">
           <el-cascader
            v-model="form.parentId"
            :options="menuOptions"
            :props="{ checkStrictly: true, value: 'id', label: 'title', emitPath: false }"
            clearable
            style="width: 100%"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="菜单名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="路由路径" prop="path">
          <el-input v-model="form.path" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item label="权限字符" prop="perms">
          <el-input v-model="form.perms" placeholder="例如 sys:menu:list" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="form.icon" placeholder="例如 el-icon-setting" />
        </el-form-item>
        <el-form-item label="显示排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 模拟数据，实际应调用后端接口 request.get('/menu/list')
const mockMenuData = [
  { id: 1, title: '系统首页', path: '/home', icon: 'el-icon-s-home', orderNum: 1 },
  
  // 原业务管理下的子菜单，现在提升为一级
  { id: 21, title: '设计师信息管理', path: '/employees', icon: 'el-icon-user', orderNum: 2 },
  { id: 22, title: '商品分类管理', path: '/category', icon: 'el-icon-menu', orderNum: 3 },
  { id: 23, title: '装修商品管理', path: '/product', icon: 'el-icon-goods', orderNum: 4 },
  { id: 24, title: '装修作品管理', path: '/work', icon: 'el-icon-picture-outline', orderNum: 5 },
  { id: 25, title: '订单管理', path: '/order', icon: 'el-icon-document', orderNum: 6 },

  // 系统管理保持层级
  {
    id: 3, title: '系统管理', path: 'sys-sub', icon: 'el-icon-setting', orderNum: 7,
    children: [
      { id: 31, parentId: 3, title: '用户管理', path: '/sys/users', orderNum: 1 },
      { id: 32, parentId: 3, title: '角色管理', path: '/sys/roles', orderNum: 2 },
      { id: 33, parentId: 3, title: '菜单管理', path: '/sys/menus', orderNum: 3 },
    ]
  },
  
  { id: 4, title: '个人中心', path: '/profile', icon: 'el-icon-user-solid', orderNum: 8 }
];

export default {
  name: "Menu",
  data() {
    return {
      loading: false,
      tableData: [], // 菜单树数据
      menuOptions: [], // 级联选择器数据
      open: false,
      title: "",
      form: {},
      rules: {
        title: [{ required: true, message: "菜单名称不能为空", trigger: "blur" }],
        path: [{ required: true, message: "路由地址不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      // TODO: 替换为 request.get('/menu/list')
      setTimeout(() => {
        this.tableData = JSON.parse(JSON.stringify(mockMenuData));
        this.loading = false;
      }, 500);
    },
    getTreeselect() {
      // 获取菜单下拉树结构
      this.menuOptions = [];
      const menu = { id: 0, title: '主类目', children: [] };
      menu.children = this.tableData;
      this.menuOptions.push(menu);
    },
    handleAdd(row) {
      this.getTreeselect();
      this.form = { id: undefined, parentId: 0, title: undefined, icon: undefined, orderNum: 0, path: '' };
      if (row != null && row.id) {
        this.form.parentId = row.id;
      }
      this.open = true;
      this.title = "添加菜单";
    },
    handleEdit(row) {
      this.getTreeselect();
      this.form = { ...row };
      this.open = true;
      this.title = "修改菜单";
    },
    handleDelete(row) {
      this.$confirm('确认删除?', "警告", { type: "warning" }).then(() => {
        // TODO: request.post('/menu/delete', [row.id])
        this.$message.success("模拟删除成功");
        this.getList();
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // TODO: request.post('/menu/save', this.form)
          this.$message.success("模拟保存成功");
          this.open = false;
          this.getList();
        }
      });
    }
  }
};
</script>
<style scoped>
.toolbar { margin-bottom: 20px; }
.text-danger { color: #f56c6c; }
</style>
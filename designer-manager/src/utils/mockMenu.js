// src/utils/mockMenu.js
export const mockMenuData = [
  { id: 1, title: '系统首页', path: '/home', icon: 'el-icon-s-home', orderNum: 1 },
  { id: 21, title: '设计师信息管理', path: '/employees', icon: 'el-icon-user', orderNum: 2 },
  { id: 22, title: '公司信息管理', path: '/company', icon: 'el-icon-office-building', orderNum: 6.5 },
  { id: 23, title: '商品分类管理', path: '/category', icon: 'el-icon-menu', orderNum: 3 },
  { id: 24, title: '装修商品管理', path: '/product', icon: 'el-icon-goods', orderNum: 4 },
  { id: 25, title: '装修作品管理', path: '/work', icon: 'el-icon-picture-outline', orderNum: 5 },
  { id: 26, title: '订单管理', path: '/order', icon: 'el-icon-document', orderNum: 6 },
  
  // --- 新增菜单项 ---
  // { id: 26, title: '公司信息管理', path: '/company', icon: 'el-icon-office-building', orderNum: 6.5 },
  // ----------------
  
  {
    id: 3, title: '系统管理', path: 'sys-sub', icon: 'el-icon-setting', orderNum: 7,
    children: [
      { id: 31, parentId: 3, title: '用户管理', path: '/sys/users', orderNum: 1 },
      { id: 32, parentId: 3, title: '角色管理', path: '/sys/roles', orderNum: 2 },
      { id: 33, parentId: 3, title: '菜单管理', path: '/sys/menus', orderNum: 3 },
	  { id: 34, parentId: 3, title: '轮播图管理', path: '/sys/carousel', orderNum: 4 },
    ]
  },
  { id: 4, title: '个人中心', path: '/profile', icon: 'el-icon-user-solid', orderNum: 8 }
];
import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/layout/index.vue'

Vue.use(VueRouter)

const routes = [
  // 1. 登录页
  {
    path: '/login',
    component: () => import('@/views/Login.vue'),
    hidden: true
  },

  // 2. 首页 (根路径重定向)
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { title: '系统首页', icon: 'el-icon-s-home' }
      }
    ]
  },

  // 3. 业务管理模块 (商品、作品、订单、设计师)
  // 注意：这里使用 path: '/' 让子路由直接显示在根路径下，例如 /product
  {
    path: '/',
    component: Layout,
    meta: { title: '业务管理', icon: 'el-icon-menu' },
    children: [
      {
        path: 'product',
        name: 'Product',
        component: () => import('@/views/business/Product.vue'),
        meta: { title: '装修商品管理' }
      },
      {
        path: 'work',
        name: 'Work',
        component: () => import('@/views/business/Work.vue'),
        meta: { title: '装修作品管理' }
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('@/views/business/Order.vue'),
        meta: { title: '订单管理' }
      },
      // 如果你也重构了设计师页面(employees.html)，请取消下面注释并创建对应文件
      {
        path: 'employees',
        name: 'Employees',
        component: () => import('@/views/business/Employees.vue'), 
        meta: { title: '设计师信息管理' }
      },
	  {
	    path: 'category', // 路由路径
	    name: 'Category',
	    component: () => import('@/views/business/Category.vue'),
	    meta: { title: '商品分类管理' }
	  },
	  // --- 新增：公司信息管理 ---
	  {
	    path: 'company',
	    name: 'Gongsixinxi',
	    component: () => import('@/views/business/Gongsixinxi.vue'),
	    meta: { title: '公司信息管理' }
	  },
	  {
	      path: 'profile',
	      name: 'Profile',
	      component: () => import('@/views/Profile.vue'), // 确保路径对应
	      meta: { title: '个人中心' }
	    }
    ]
  },

  // 4. 系统管理模块 (用户、角色、轮播图)
  {
    path: '/sys',
    component: Layout,
    meta: { title: '系统管理', icon: 'el-icon-setting' },
    children: [
      {
        path: 'users',
        name: 'Users',
        component: () => import('@/views/sys/Users.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'roles',
        name: 'Roles',
        component: () => import('@/views/sys/Roles.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'carousel', // 对应 system.html (轮播图配置)
        name: 'carousel',
        component: () => import('@/views/sys/carousel.vue'),
        meta: { title: '轮播图管理' }
      },
	  {
	      path: 'menus',
	      name: 'Menus',
	      component: () => import('@/views/sys/Menu.vue'),
	      meta: { title: '菜单管理' }
	    },
    ]
  },
  
  // 5. 个人中心 (可选)
  {
    path: '/',
    component: Layout,
    children: [
      {
        path: 'profile',
        name: 'Profile',
        // 如果还没有 Profile.vue，可以先指向 Home 或者创建一个简单的占位组件
        component: () => import('@/views/Home.vue'), 
        meta: { title: '个人中心' }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history', // 去掉 URL 中的 #
  base: process.env.BASE_URL,
  routes
})

// 路由守卫：防止未登录访问
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.path === '/login') {
    // 如果已登录，访问登录页直接跳首页
    if (token) {
      next({ path: '/' })
    } else {
      next()
    }
  } else {
    // 访问非登录页
    if (!token) {
      // 无 token，强制跳转登录页，并记录重定向地址
      next(`/login?redirect=${to.path}`)
    } else {
      next()
    }
  }
})

export default router
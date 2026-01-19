<template>
  <el-container class="app-wrapper">
    <el-aside width="220px" class="sidebar-container">
      <el-menu
        :default-active="$route.path"
        background-color="#ffffff"
        text-color="#606266"
        active-text-color="#409EFF"
        router
        unique-opened
        class="custom-menu"
      >
        <template v-for="menu in menuList">
          <el-submenu v-if="menu.children && menu.children.length" :index="menu.path || menu.id + ''" :key="menu.id">
            <template slot="title">
              <i :class="menu.icon"></i>
              <span>{{ menu.title }}</span>
            </template>
            <el-menu-item 
              v-for="sub in menu.children" 
              :index="sub.path" 
              :key="sub.id"
            >
              <i v-if="sub.icon" :class="sub.icon"></i>
              <span>{{ sub.title }}</span>
            </el-menu-item>
          </el-submenu>

          <el-menu-item v-else :index="menu.path" :key="menu.id">
            <i :class="menu.icon"></i>
            <span slot="title">{{ menu.title }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <el-container class="main-content-wrapper">
      <el-header class="navbar">
        <div class="header-left"></div>
        <div class="header-center">家居数字化管理平台</div>
        <div class="header-right">
          <el-dropdown @command="handleCommand" trigger="click">
            <div class="user-dropdown-link">
              <i class="el-icon-info"></i>
              <span class="user-role">{{ role || '系统管理员' }}</span>
              <i class="el-icon-caret-bottom"></i>
            </div>
            <el-dropdown-menu slot="dropdown" class="user-dropdown-menu">
              <el-dropdown-item command="clearCache">清除缓存</el-dropdown-item>
              <el-dropdown-item command="logout" divided>注销登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>

      <div class="tags-view-container">
        <div class="tags-scroll-wrapper">
          <router-link
            v-for="(tag, index) in visitedViews"
            :key="tag.path"
            :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }"
            tag="span"
            class="tags-view-item"
            :class="isActive(tag) ? 'active' : ''"
            @click.native="handleTagClick(tag)"
            @contextmenu.prevent.native="openMenu(tag, $event)"
          >
            {{ tag.title }}
            <span 
              v-if="tag.path !== '/home'" 
              class="el-icon-close" 
              @click.prevent.stop="closeSelectedTag(tag, index)"
            ></span>
          </router-link>
        </div>

        <ul v-show="visible" :style="{left:left+'px',top:top+'px'}" class="contextmenu">
          <li @click="closeOthersTags">关闭其他</li>
          <li @click="closeAllTags">关闭所有</li>
        </ul>
      </div>

      <el-main>
        <transition name="fade-transform" mode="out-in">
          <router-view />
        </transition>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mockMenuData } from '@/utils/mockMenu'
import request from '@/utils/request'

export default {
  name: 'Layout',
  data() {
    return {
      username: localStorage.getItem('username') || '用户',
      role: localStorage.getItem('role') || '用户',
      visitedViews: [],
      menuList: [],
      visible: false,
      top: 0,
      left: 0,
      selectedTag: {}
    }
  },
  watch: {
    $route() {
      this.addTags()
    },
    visible(value) {
      if (value) {
        document.body.addEventListener('click', this.closeMenu)
      } else {
        document.body.removeEventListener('click', this.closeMenu)
      }
    }
  },
  created() {
    this.getUserInfo()
    this.addTags()
  },
  methods: {
    getUserInfo() {
      request.get('/users/session').then(res => {
        if (res.code === 0 && res.data) {
          this.username = res.data.username
          this.role = res.data.role
          localStorage.setItem('username', res.data.username)
          localStorage.setItem('role', res.data.role)
          
          if (this.role === '管理员') {
            this.menuList = mockMenuData;
          } else {
            if (res.menus && res.menus.length > 0) {
                this.menuList = res.menus;
            } else {
                const allowedIds = [1, 25, 4]; 
                this.menuList = mockMenuData.filter(m => allowedIds.includes(m.id));
            }
          }
        }
      }).catch(err => {
        console.error('获取用户信息失败', err)
        this.menuList = mockMenuData; 
      })
    },
    addTags() {
      const { name, meta, path, fullPath } = this.$route
      if (name && meta && meta.title) {
        const isExist = this.visitedViews.some(v => v.path === path)
        if (!isExist) {
          this.visitedViews.push({
            name,
            path,
            fullPath,
            title: meta.title
          })
        }
      }
    },
    isActive(tag) {
      return tag.path === this.$route.path
    },
    handleTagClick(tag) {},
    closeSelectedTag(view, index) {
      this.visitedViews.splice(index, 1)
      if (this.isActive(view)) {
        const latestView = this.visitedViews.slice(-1)[0]
        if (latestView) {
          this.$router.push(latestView.path)
        } else {
          this.$router.push('/home')
        }
      }
    },
    openMenu(tag, e) {
      const menuMinWidth = 105
      const offsetLeft = this.$el.getBoundingClientRect().left 
      const offsetWidth = this.$el.offsetWidth 
      const maxLeft = offsetWidth - menuMinWidth 
      const left = e.clientX - offsetLeft + 15 

      if (left > maxLeft) {
        this.left = maxLeft
      } else {
        this.left = left
      }

      this.top = e.clientY
      this.visible = true
      this.selectedTag = tag
    },
    closeMenu() {
      this.visible = false
    },
    closeOthersTags() {
      if (this.$route.path !== this.selectedTag.path) {
        this.$router.push(this.selectedTag).catch(err => {})
      }
      this.visitedViews = this.visitedViews.filter(tag => {
        return tag.path === this.selectedTag.path || tag.path === '/home'
      })
    },
    closeAllTags() {
      this.visitedViews = this.visitedViews.filter(tag => tag.path === '/home')
      this.$router.push('/')
    },
    handleCommand(command) {
      if (command === 'logout') {
        this.logout();
      } else if (command === 'clearCache') {
        this.$message.success('缓存已清除');
      }
    },
    logout() {
      this.$confirm('确定退出登录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        localStorage.removeItem('role')
        this.$router.push('/login')
      })
    }
  }
}
</script>

<style scoped>
.app-wrapper {
  height: 100vh;
  background-color: #f0f2f5;
}
.sidebar-container {
  background-color: #ffffff;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.05);
  z-index: 10;
  display: flex;
  flex-direction: column;
}
.custom-menu { border-right: none; }
::v-deep .el-menu-item.is-active {
  background-color: #e6f7ff !important;
  border-right: 3px solid #1890ff;
}
.main-content-wrapper {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
}
.navbar {
  height: 60px;
  background: #ffffff;
  display: flex;
  align-items: center;
  padding: 0 24px;
  border-bottom: 1px solid #e6e6e6;
  z-index: 9;
}
.header-left { flex: 1; }
.header-center {
  flex: 2;
  text-align: center;
  font-size: 22px;
  font-weight: 500;
  color: #333;
  letter-spacing: 1px;
}
.header-right {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.user-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
  padding: 0 10px;
  height: 100%;
  color: #606266;
  font-size: 14px;
}

/* --- 样式优化：标签栏 --- */
.tags-view-container {
  height: 34px; 
  width: 100%;
  background: transparent;
  border-bottom: none;
  display: flex;
  align-items: center;
  padding: 0 15px;
  /* 关键：去掉下边距，减小与内容的距离 */
  margin-top: 5px; 
  margin-bottom: 0;
}

.tags-scroll-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  overflow-x: auto;
  width: 100%;
}

.tags-view-item {
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  height: 26px; 
  line-height: 26px;
  border: 1px solid #d8dce5;
  color: #495060;
  background: #fff;
  padding: 0 10px;
  font-size: 12px;
  border-radius: 3px;
  transition: all 0.3s;
}
.tags-view-item.active {
  background-color: #e6f7ff;
  color: #1890ff;
  border-color: #1890ff;
}
.tags-view-item:hover {
  color: #1890ff;
  border-color: #b3d8ff;
}
.tags-view-item .el-icon-close {
  width: 16px;
  height: 16px;
  line-height: 16px;
  border-radius: 50%;
  text-align: center;
  transition: all .3s cubic-bezier(.645, .045, .355, 1);
  margin-left: 5px;
  font-size: 12px;
}
.tags-view-item .el-icon-close:hover {
  background-color: #1890ff;
  color: #fff;
}

/* --- 样式优化：主内容区域 --- */
.el-main {
  background-color: #f0f2f5;
  /* 关键：顶部内边距减小到 5px，紧贴标签栏 */
  padding: 5px 10px 20px 10px; 
  overflow-y: auto;
}

.contextmenu {
  margin: 0;
  background: #fff;
  z-index: 3000;
  position: fixed;
  list-style-type: none;
  padding: 5px 0;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 400;
  color: #333;
  box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, .3);
}
.contextmenu li {
  margin: 0;
  padding: 7px 16px;
  cursor: pointer;
}
.contextmenu li:hover {
  background: #eee;
}
</style>

<style>
/* 全局表格表头样式 */
.el-table th {
  background-color: #f5f7fa !important;
  color: #606266;
  font-weight: 600;
}
.el-table thead.is-group th {
  background: #f5f7fa !important;
}
</style>
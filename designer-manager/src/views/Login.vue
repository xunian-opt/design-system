<template>
  <div class="login-container" :style="{ backgroundImage: `url(${bgImage})` }">
    
    <div class="login-box">
      <div class="header">
        <h1>家居数字化管理平台</h1>
        <h2>Smart management system</h2>
      </div>

      <el-form :model="loginForm" :rules="rules" ref="loginForm" size="large">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入账号"
            prefix-icon="el-icon-user"
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            @keyup.enter.native="handleLogin"
            show-password
          ></el-input>
        </el-form-item>

        <div class="button-group">
          <el-button 
            type="primary" 
            :loading="loading" 
            @click="handleLogin" 
            class="login-button"
          >登 录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import qs from 'qs' // 引入 qs 库用于序列化参数

export default {
  name: 'Login',
  data() {
    return {
      // 引用本地图片资源
      bgImage: require('@/assets/loginbk.jpg'), 
      
      loginForm: {
        username: '',
        password: ''
      },
      loading: false,
      rules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          
          // 修改点：使用 qs.stringify 转换数据格式
          // 后端 UsersController.login 接收的是 Form Data 参数
          request.post('/users/login', qs.stringify(this.loginForm), {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          })
          .then(res => {
            if (res.code === 0) {
              // 登录成功
              localStorage.setItem('token', res.token)
              this.$message.success('登录成功')
              // 跳转首页
              this.$router.push('/')
            } else {
              this.$message.error(res.msg || '登录失败')
            }
          })
          .catch(err => {
            console.error(err)
            this.$message.error('网络错误或服务器异常')
          })
          .finally(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
/* 全屏背景容器 */
.login-container {
  height: 100vh;
  width: 100%;
  background-size: cover;
  background-position: center center;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  
  /* 深色遮罩层 */
  &::before {
    content: '';
    position: absolute;
    top: 0; left: 0; right: 0; bottom: 0;
    background: rgba(0, 20, 40, 0.4); 
  }
}

/* 毛玻璃登录框 */
.login-box {
  position: relative;
  width: 440px;
  padding: 60px 45px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.3);
  text-align: center;

  .header {
    margin-bottom: 50px;
    h1 {
      font-size: 34px;
      font-weight: 600;
      color: #fff;
      margin: 0 0 15px;
      letter-spacing: 2px;
      text-shadow: 0 2px 4px rgba(0,0,0,0.2);
    }
    h2 {
      font-size: 14px;
      font-weight: 400;
      color: rgba(255, 255, 255, 0.7);
      margin: 0;
      text-transform: uppercase;
      letter-spacing: 3px;
    }
  }

  /* 输入框样式定制 */
  ::v-deep .el-form-item {
    margin-bottom: 30px;
  }

  ::v-deep .el-input {
    input {
      background: transparent !important;
      border: none;
      border-bottom: 2px solid rgba(255, 255, 255, 0.3);
      border-radius: 0;
      color: #fff;
      padding-left: 40px;
      height: 55px;
      line-height: 55px;
      font-size: 16px;
      transition: all 0.3s;

      &::placeholder {
        color: rgba(255, 255, 255, 0.5);
      }

      &:focus {
        border-bottom-color: #fff;
      }
    }

    .el-input__prefix {
      left: 5px;
      i {
        color: #fff;
        font-size: 20px;
        line-height: 55px;
      }
    }
    
    .el-input__suffix i {
        color: rgba(255, 255, 255, 0.5);
        line-height: 55px;
    }
  }

  .button-group {
    margin-top: 50px;
  }

  /* 登录按钮样式 */
  .login-button {
    width: 100%;
    height: 55px;
    border-radius: 55px;
    border: none;
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    font-size: 20px;
    font-weight: 600;
    letter-spacing: 8px;
    box-shadow: 0 10px 30px rgba(0, 242, 254, 0.3);
    transition: all 0.3s;

    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 15px 35px rgba(0, 242, 254, 0.5);
    }
    &:active {
      transform: translateY(-1px);
    }
  }
}
</style>
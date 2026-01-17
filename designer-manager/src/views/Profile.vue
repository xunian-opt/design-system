<template>
  <div class="app-container">
    <!-- <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item>基础功能</el-breadcrumb-item>
      <el-breadcrumb-item>个人中心</el-breadcrumb-item>
    </el-breadcrumb> -->

    <div class="profile-wrapper">
      <el-card class="box-card profile-card">
        <div slot="header" class="clearfix header-center">
          <div class="avatar-large">
            <i class="el-icon-user-solid"></i>
          </div>
          <h2>{{ form.role || '用户' }}资料设置</h2>
        </div>

        <el-form :model="form" :rules="rules" ref="form" label-width="100px" label-position="left">
          <el-form-item label="用户名">
            <el-input v-model="form.username" disabled></el-input>
          </el-form-item>
          
          <el-form-item label="角色">
            <el-input v-model="form.role" disabled></el-input>
          </el-form-item>
		  
          <el-form-item label="新密码" prop="newPass">
            <el-input type="password" v-model="form.newPass" placeholder="请输入新密码" show-password></el-input>
          </el-form-item>
          
          <el-form-item label="确认密码" prop="confirmPass">
            <el-input type="password" v-model="form.confirmPass" placeholder="请再次输入新密码" show-password></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" class="btn-block" :loading="loading" @click="updateProfile">保存修改</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'Profile',
  data() {
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.newPass) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      loading: false,
      form: {
        id: '',
        username: '',
        role: '',
        newPass: '',
        confirmPass: ''
      },
      rules: {
        newPass: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
        ],
        confirmPass: [
          { required: true, validator: validatePass2, trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      // 从 session 接口获取最新用户信息
      request.get('/users/session').then(res => {
        if (res.code === 0 && res.data) {
          this.form.id = res.data.id
          this.form.username = res.data.username
          this.form.role = res.data.role
        }
      })
    },
    updateProfile() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true
          const data = {
            id: this.form.id,
            username: this.form.username,
            password: this.form.newPass,
            role: this.form.role
          }
          
          request.post('/users/update', data).then(res => {
            this.loading = false
            if (res.code === 0) {
              this.$message.success('密码修改成功，请重新登录')
              setTimeout(() => {
                localStorage.removeItem('token')
                this.$router.push('/login')
              }, 1500)
            } else {
              this.$message.error(res.msg || '修改失败')
            }
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.profile-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}
.profile-card {
  width: 100%;
  max-width: 500px;
}
.header-center {
  text-align: center;
}
.avatar-large {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: #f0f2f5;
  margin: 0 auto 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  color: #909399;
}
.btn-block {
  width: 100%;
}
</style>
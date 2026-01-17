import axios from 'axios'
import { Message } from 'element-ui'
import router from '@/router'

// 创建 axios 实例
const service = axios.create({
  baseURL: '/springboot2z04j', // 对应 navbar.js 中的 getApiBase
  timeout: 10000 // 请求超时时间
})

// request 拦截器
service.interceptors.request.use(
  config => {
    // 对应 navbar.js 中从 localStorage 获取 token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Token'] = token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    // 假设后端 code 为 0 代表成功
    if (res.code !== 0) {
      Message({
        message: res.msg || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      
      // 401: 未登录
      if (res.code === 401) {
        localStorage.removeItem('token')
        router.push('/login')
      }
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      return res
    }
  },
  error => {
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
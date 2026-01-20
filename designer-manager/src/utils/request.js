import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import router from '@/router' // 引入路由实例

// 1. 定义一个变量，用于标识是否正在显示“重新登录”的弹窗
// 防止并发请求导致弹窗重复弹出
let isReloginShow = false

// 创建 axios 实例
const service = axios.create({
  baseURL: '/springboot2z04j', // 根据您的后端前缀调整
  timeout: 10000 // 请求超时时间
})

// request 拦截器
service.interceptors.request.use(
  config => {
    // 发送请求前，将 token 加入 header
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Token'] = token
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果后端返回的是二进制流（下载文件），直接返回
    if (response.config.responseType === 'blob' || response.config.responseType === 'arraybuffer') {
      return res
    }

    // code === 0 表示成功
    if (res.code === 0) {
      return res
    } else {
      // ---------------------------------------------------------------------------------
      // 修改点 1：增加 "请先登录" 的判断，防止它被当成普通报错弹出红色提示
      // ---------------------------------------------------------------------------------
      if (res.code === 401 || res.msg.includes("失效") || res.msg.includes("未登录") || res.msg.includes("请先登录")) {
        
        // ---------------------------------------------------------------------------------
        // 修改点 2：区分 "首次访问(无Token)" 和 "Token过期"
        // ---------------------------------------------------------------------------------
        const currentToken = localStorage.getItem('token');

        // 如果本地根本没有 Token，说明是第一次进，或者已经退出了
        // 直接跳转登录页，不要弹窗干扰用户
        if (!currentToken) {
             // 避免重复跳转，如果当前已经是登录页就不跳了
             if (router.currentRoute.path !== '/login') {
                router.push(`/login?redirect=${router.currentRoute.fullPath}`)
             }
             return Promise.reject(new Error('请重新登录'))
        }

        // 如果本地有 Token，但是后端返回 401/失效，说明是过期了 -> 弹窗提示
        if (!isReloginShow) {
            isReloginShow = true; // 上锁
            
            MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
              confirmButtonText: '重新登录',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              isReloginShow = false; 
              
              localStorage.removeItem('token')
              localStorage.removeItem('username')
              localStorage.removeItem('role')
              router.push(`/login?redirect=${router.currentRoute.fullPath}`)
              location.reload()
            }).catch(() => {
              isReloginShow = false;
            })
        }
      } else {
        // 其他非认证类的错误，才显示红色提示
        Message({
          message: res.msg || 'Error',
          type: 'error',
          duration: 3 * 1000
        })
      }
      return Promise.reject(new Error(res.msg || 'Error'))
    }
  },
  error => {
    console.log('err' + error)
    let message = error.message
    if (message == "Network Error") {
        message = "后端接口连接异常";
    } else if (message.includes("timeout")) {
        message = "系统接口请求超时";
    } else if (message.includes("Request failed with status code")) {
        message = "系统接口" + message.substr(message.length - 3) + "异常";
    }
    
    // 捕获 HTTP 401 状态码 (应对后端直接返回 Http Status 401 的情况)
    if (error.response && error.response.status === 401) {
        localStorage.removeItem('token')
        
        // 同样逻辑：如果有 token 才提示，没 token 直接跳
        const token = localStorage.getItem('token')
        if (token) {
             Message({
                message: '登录已过期，请重新登录',
                type: 'error',
                duration: 3 * 1000
            })
        }
        router.push('/login')
    } else {
        Message({
            message: message,
            type: 'error',
            duration: 5 * 1000
        })
    }
    return Promise.reject(error)
  }
)

export default service
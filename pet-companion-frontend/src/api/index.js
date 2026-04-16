// API请求封装
import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: '/api', // 使用Vite代理，会被代理到后端服务
  timeout: 10000, // 请求超时时间，10秒
  headers: {
    'Content-Type': 'application/json' // 默认请求头，使用JSON格式
  }
})

// 请求拦截器，用于在请求发送前处理
api.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    // 如果有token，添加到请求头
    if (token) {
      config.headers.Authorization = `Bearer ${token}` // 使用Bearer token格式
    }
    return config // 返回处理后的配置
  },
  error => {
    // 处理请求错误
    return Promise.reject(error)
  }
)

// 响应拦截器，用于在收到响应后处理
api.interceptors.response.use(
  response => {
    // 直接返回响应数据，简化使用
    return response.data
  },
  error => {
    // 处理错误响应
    if (error.response) {
      // 服务器返回错误状态码
      switch (error.response.status) {
        case 401:
          // 未授权，跳转到登录页面
          localStorage.removeItem('token')
          localStorage.removeItem('user')
          window.location.href = '/login'
          break
        case 403:
          // 禁止访问
          console.error('禁止访问')
          break
        case 404:
          // 资源不存在
          console.error('资源不存在')
          break
        case 500:
          // 服务器错误
          console.error('服务器错误')
          break
        default:
          console.error('请求失败')
      }
    } else if (error.request) {
      // 请求已发出，但没有收到响应
      console.error('网络错误')
    } else {
      // 请求配置出错
      console.error('请求配置出错')
    }
    return Promise.reject(error)
  }
)

// 导出api实例
export default api

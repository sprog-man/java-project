import axios from './axios'

// 用户API
export const userAPI = {
  // 登录
  login: (credentials) => {
    return axios.post('/user/login', credentials)
  },
  
  // 注册
  register: (userData) => {
    return axios.post('/user/register', userData)
  },
  
  // 获取用户信息
  getUserInfo: () => {
    return axios.get('/user/info')
  },
  
  // 更新用户信息
  updateUserInfo: (userData) => {
    return axios.put('/user/info', userData)
  }
}

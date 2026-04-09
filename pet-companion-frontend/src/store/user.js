// 导入Pinia的defineStore函数，用于创建状态管理仓库
import { defineStore } from 'pinia'
// 导入axios实例，用于发送API请求
import axios from '../api'

// 创建用户状态管理仓库
export const useUserStore = defineStore('user', {
  // 状态定义
  state: () => ({
    user: null, // 用户信息
    token: localStorage.getItem('token'), // 从localStorage获取token
    isLoggedIn: !!localStorage.getItem('token'), // 是否已登录
    userType: localStorage.getItem('userType') // 用户类型
  }),
  
  // 计算属性
  getters: {
    getUser: (state) => state.user, // 获取用户信息
    getToken: (state) => state.token, // 获取token
    getIsLoggedIn: (state) => state.isLoggedIn, // 获取登录状态
    getUserType: (state) => {
      // 转换userType为字符串，方便比较
      if (state.userType === 0 || state.userType === '0') {
        return 'USER'; // 普通用户
      } else if (state.userType === 1 || state.userType === '1') {
        return 'PROVIDER'; // 服务提供者
      }
      return state.userType;
    }
  },
  
  // 动作方法
  actions: {
    // 登录
    async login(credentials) {
      try {
        console.log('开始登录', credentials)
        // 构建符合后端要求的登录数据
        const loginData = {
          phone: credentials.username, // 前端使用username字段，后端期望phone字段
          password: credentials.password
        }
        console.log('登录数据', loginData)
        console.log('API实例', axios)
        console.log('API实例baseURL', axios.defaults.baseURL)
        // 发送登录请求
        const response = await axios.post('/user/login', loginData)
        console.log('登录响应:', response)
        console.log('响应数据结构:', response.data)
        // 后端返回的是Result对象，数据在data字段中，而LoginResponse对象又在data字段中
        if (response.data && response.data.data) {
          const { token, userInfo } = response.data.data
          console.log('获取到token:', token)
          console.log('获取到userInfo:', userInfo)
          
          // 存储token和用户信息到localStorage
          localStorage.setItem('token', token)
          localStorage.setItem('userType', userInfo.userType)
          localStorage.setItem('userInfo', JSON.stringify(userInfo))
          
          // 更新状态
          this.token = token
          this.user = userInfo
          this.isLoggedIn = true
          this.userType = userInfo.userType
          
          console.log('登录成功，token:', token)
          console.log('登录成功，用户信息:', userInfo)
          return response
        } else {
          console.error('响应数据格式错误:', response.data)
          throw new Error('响应数据格式错误')
        }
      } catch (error) {
        console.error('登录失败', error)
        console.error('错误响应:', error.response)
        throw error
      }
    },
    
    // 注册
    async register(userData) {
      try {
        // 构建符合后端要求的注册数据
        const registerData = {
          username: userData.username,
          nickname: userData.nickname,
          phone: userData.phone,
          password: userData.password
        }
        // 发送注册请求
        const response = await axios.post('/user/register', registerData)
        return response.data
      } catch (error) {
        console.error('注册失败', error)
        throw error
      }
    },

    // 获取用户信息
    async getUserInfo() {
      try {
        const response = await axios.get('/user/info')
        console.log('getUserInfo响应:', response)

        // 适配响应拦截器返回的格式
        if (response.code === 200) {
          if (response.data) {
            this.user = response.data
            // 缓存用户信息到localStorage
            localStorage.setItem('userInfo', JSON.stringify(response.data))
            return response.data
          } else {
            console.error('用户数据为空')
            throw new Error('用户数据为空')
          }
        } else {
          console.error('获取用户信息失败:', response.message)
          throw new Error(response.message || '获取用户信息失败')
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        // 从localStorage获取缓存的用户信息
        const cachedUserInfo = localStorage.getItem('userInfo')
        if (cachedUserInfo) {
          try {
            const userData = JSON.parse(cachedUserInfo)
            this.user = userData
            console.log('从缓存获取用户信息成功')
            return userData
          } catch (e) {
            console.error('解析缓存用户信息失败:', e)
          }
        }
        throw error
      }
    },


    // 更新用户信息
    async updateUserInfo(userData) {
      try {
        // 发送更新用户信息请求
        const response = await axios.put('/user/center/modify', userData)
        console.log('updateUserInfo响应:', response)
        // 适配响应拦截器返回的格式
        if (response.code === 200) {
          // 这里后端可能不返回data，只返回成功消息
          return response
        } else {
          console.error('更新用户信息失败:', response.message)
          throw new Error(response.message || '更新用户信息失败')
        }
      } catch (error) {
        console.error('更新用户信息失败:', error)
        throw error
      }
    },
    
    // 退出登录
    logout() {
      // 从localStorage移除token和用户类型
      localStorage.removeItem('token')
      localStorage.removeItem('userType')
      
      // 重置状态
      this.token = null
      this.user = null
      this.isLoggedIn = false
      this.userType = null
    }
  }
})

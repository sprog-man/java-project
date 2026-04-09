import { defineStore } from 'pinia'
import api from '../api/index'

export const useProviderStore = defineStore('provider', {
  state: () => ({
    provider: null,
    isLoading: false,
    error: null
  }),
  
  getters: {
    getProvider: (state) => state.provider
  },
  
  actions: {
    // 获取服务提供者信息
    async getProviderInfo() {
      this.isLoading = true
      this.error = null
      try {
        // 这里应该从后端获取服务提供者信息
        // 暂时使用模拟数据
        this.provider = {
          id: 1,
          name: '张阿姨',
          phone: '138****1234',
          email: 'zhang@example.com',
          avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=provider%20avatar%2C%20simple%20style&image_size=square',
          rating: 4.8,
          orderCount: 120
        }
      } catch (error) {
        this.error = error.message
        console.error('获取服务提供者信息失败', error)
      } finally {
        this.isLoading = false
      }
    },
    
    // 申请成为服务提供者
    async applyProvider(data) {
      this.isLoading = true
      this.error = null
      try {
        // 这里应该调用后端API申请成为服务提供者
        console.log('申请成为服务提供者:', data)
        // 模拟成功
        return true
      } catch (error) {
        this.error = error.message
        console.error('申请服务提供者失败', error)
        throw error
      } finally {
        this.isLoading = false
      }
    },
    
    // 更新服务提供者信息
    async updateProviderInfo(data) {
      this.isLoading = true
      this.error = null
      try {
        // 这里应该调用后端API更新服务提供者信息
        console.log('更新服务提供者信息:', data)
        // 模拟成功
        this.provider = { ...this.provider, ...data }
        return true
      } catch (error) {
        this.error = error.message
        console.error('更新服务提供者信息失败', error)
        throw error
      } finally {
        this.isLoading = false
      }
    }
  }
})

import { defineStore } from 'pinia'
import axios from '../api/axios' // ✅ 1. 修正导入：引入 axios 实例


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
    async applyProvider(formData) {
      this.isLoading = true
      try {
        // ✅ 2. 简化逻辑：因为前端表单字段已对齐 DTO，直接发送 formData 即可
        console.log('正在向后端发送申请:', formData)

        // 注意：这里使用导入的 axios，而不是 api
        const response = await axios.post('/service-provider/apply', formData)

        if (response.data.code === 200) {
          alert('申请提交成功，请等待管理员审核')
          return response.data
        } else {
          throw new Error(response.data.msg || '申请失败')
        }
      } catch (error) {
        console.error('申请服务者失败:', error)
        throw error // 抛出错误让组件层处理提示
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

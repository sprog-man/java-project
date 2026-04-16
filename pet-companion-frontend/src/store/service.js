import { defineStore } from 'pinia'
import axios from '../api/axios'

export const useServiceStore = defineStore('service', {
  state: () => ({
    serviceTypes: [],
    currentServiceType: null,
    providers: [],
    currentProvider: null
  }),
  
  getters: {
    getServiceTypes: (state) => state.serviceTypes,
    getCurrentServiceType: (state) => state.currentServiceType,
    getProviders: (state) => state.providers,
    getCurrentProvider: (state) => state.currentProvider
  },

  actions: {
    // ✅ 新增：获取服务类型列表（对接后端接口）
    async fetchServiceTypes() {
      try {
        const response = await axios.get('/service-type/list')
        if (response.data.code === 200) {
          this.serviceTypes = response.data.data
        }
      } catch (error) {
        console.error('获取服务类型列表失败:', error)
        throw error
      }
    },

    // ✅ 完善：获取服务类型详情
    async fetchServiceTypeDetail(id) {
      try {
        console.log('Store: 正在请求服务详情, ID:', id); // 调试日志
        const response = await axios.get(`/service-type/${id}`)
        console.log('Store: 后端原始响应:', response.data); // 调试日志

        if (response.data.code === 200) {
          this.currentServiceType = response.data.data
          console.log('Store: 成功更新 currentServiceType:', this.currentServiceType);
          return response.data.data
        } else {
          console.error('Store: 业务逻辑错误:', response.data.msg);
        }
      } catch (error) {
        console.error('获取服务类型详情失败:', error)
        throw error
      }
    },
    
    // 申请成为服务提供者
    async applyProvider(providerData) {
      try {
        const response = await axios.post('/api/service/provider/apply', providerData)
        return response.data
      } catch (error) {
        throw error
      }
    },
    
    // 获取服务提供者信息
    async getProviderInfo() {
      try {
        const response = await axios.get('/api/service/provider/info')
        this.currentProvider = response.data
        return response.data
      } catch (error) {
        throw error
      }
    },
    
    // 更新服务提供者信息
    async updateProviderInfo(providerData) {
      try {
        const response = await axios.put('/api/service/provider/info', providerData)
        this.currentProvider = response.data
        return response.data
      } catch (error) {
        throw error
      }
    }
  }
})

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
    // 获取服务类型列表
    async getServiceTypeList() {
      try {
        const response = await axios.get('/api/service/type')
        this.serviceTypes = response.data
        return response.data
      } catch (error) {
        throw error
      }
    },
    
    // 获取服务类型详情
    async getServiceTypeDetail(id) {
      try {
        const response = await axios.get(`/api/service/type/${id}`)
        this.currentServiceType = response.data
        return response.data
      } catch (error) {
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

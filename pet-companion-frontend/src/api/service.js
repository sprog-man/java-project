import axios from './axios'

// 服务API
export const serviceAPI = {
  // 获取服务类型列表
  getServiceTypeList: () => {
    return axios.get('/service/type')
  },
  
  // 获取服务类型详情
  getServiceTypeDetail: (id) => {
    return axios.get(`/service/type/${id}`)
  },
  
  // 申请成为服务提供者
  applyProvider: (providerData) => {
    return axios.post('/service/provider/apply', providerData)
  },
  
  // 获取服务提供者信息
  getProviderInfo: () => {
    return axios.get('/service/provider/info')
  },
  
  // 更新服务提供者信息
  updateProviderInfo: (providerData) => {
    return axios.put('/service/provider/info', providerData)
  }
}

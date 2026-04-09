import axios from './axios'

// 订单API
export const orderAPI = {
  // 创建订单
  createOrder: (orderData) => {
    return axios.post('/order', orderData)
  },
  
  // 获取订单列表
  getOrderList: () => {
    return axios.get('/order')
  },
  
  // 获取订单详情
  getOrderDetail: (id) => {
    return axios.get(`/order/${id}`)
  },
  
  // 取消订单
  cancelOrder: (id) => {
    return axios.put(`/order/${id}/cancel`)
  },
  
  // 接受订单
  acceptOrder: (id) => {
    return axios.put(`/order/${id}/accept`)
  },
  
  // 开始服务
  startOrder: (id) => {
    return axios.put(`/order/${id}/start`)
  },
  
  // 完成服务
  completeOrder: (id) => {
    return axios.put(`/order/${id}/complete`)
  },
  
  // 获取服务提供者订单列表
  getProviderOrderList: () => {
    return axios.get('/order/provider')
  },
  
  // 获取待接单订单列表
  getPendingOrderList: () => {
    return axios.get('/order/pending')
  }
}

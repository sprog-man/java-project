import axios from './axios'

// 支付API
export const paymentAPI = {
  // 创建支付
  createPayment: (paymentData) => {
    return axios.post('/payment/create', paymentData)
  },
  
  // 获取支付记录
  getPaymentDetail: (id) => {
    return axios.get(`/payment/${id}`)
  },
  
  // 根据订单ID获取支付记录
  getPaymentByOrderId: (orderId) => {
    return axios.get(`/payment/order/${orderId}`)
  },
  
  // 获取用户支付记录列表
  getPaymentList: () => {
    return axios.get('/payment/list')
  },
  
  // 支付回调
  paymentCallback: (callbackData) => {
    return axios.post('/payment/callback', callbackData)
  }
}

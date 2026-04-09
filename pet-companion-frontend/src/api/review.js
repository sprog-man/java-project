import axios from './axios'

// 评价API
export const reviewAPI = {
  // 创建评价
  createReview: (reviewData) => {
    return axios.post('/review/create', reviewData)
  },
  
  // 获取评价详情
  getReviewDetail: (id) => {
    return axios.get(`/review/${id}`)
  },
  
  // 根据订单ID获取评价
  getReviewByOrderId: (orderId) => {
    return axios.get(`/review/order/${orderId}`)
  },
  
  // 回复评价
  replyReview: (id, replyData) => {
    return axios.post(`/review/reply/${id}`, replyData)
  },
  
  // 获取用户评价列表
  getUserReviewList: () => {
    return axios.get('/review/user/list')
  },
  
  // 获取服务提供者评价列表
  getProviderReviewList: () => {
    return axios.get('/review/provider/list')
  }
}

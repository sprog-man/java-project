import { defineStore } from 'pinia'
import api from '../api/index'

export const useReviewStore = defineStore('review', {
  state: () => ({
    reviews: [],
    currentReview: null,
    isLoading: false,
    error: null
  }),
  
  getters: {
    getReviews: (state) => state.reviews,
    getCurrentReview: (state) => state.currentReview
  },
  
  actions: {
    // 获取用户评价列表
    async getUserReviews() {
      this.isLoading = true
      this.error = null
      try {
        // 这里应该从后端获取用户评价列表
        // 暂时使用模拟数据
        this.reviews = [
          {
            id: 1,
            serviceName: '宠物陪伴',
            serviceTime: '2026-04-01 14:00-16:00',
            rating: 5,
            content: '服务非常好，阿姨很专业，对我家狗狗很有耐心，下次还会预约！',
            images: [
              'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=happy%20dog%2C%20cute&image_size=square'
            ],
            createTime: '2026-04-01 16:30:00',
            orderNo: '20260401001'
          },
          {
            id: 2,
            serviceName: '宠物遛弯',
            serviceTime: '2026-03-28 09:00-10:00',
            rating: 4,
            content: '服务不错，准时到达，狗狗很开心。',
            images: [],
            createTime: '2026-03-28 10:30:00',
            orderNo: '20260328001'
          }
        ]
      } catch (error) {
        this.error = error.message
        console.error('获取用户评价列表失败', error)
      } finally {
        this.isLoading = false
      }
    },
    
    // 获取服务提供者评价列表
    async getProviderReviews() {
      this.isLoading = true
      this.error = null
      try {
        // 这里应该从后端获取服务提供者评价列表
        // 暂时使用模拟数据
        this.reviews = [
          {
            id: 1,
            userAvatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%2C%20simple%20style&image_size=square',
            userName: '王先生',
            rating: 5,
            content: '服务非常好，阿姨很专业，对我家狗狗很有耐心，下次还会预约！',
            images: [
              'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=happy%20dog%2C%20cute&image_size=square'
            ],
            createTime: '2026-04-05 15:00:00',
            orderNo: '20260401001'
          },
          {
            id: 2,
            userAvatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%2C%20simple%20style&image_size=square',
            userName: '李女士',
            rating: 4,
            content: '服务不错，准时到达，狗狗很开心。',
            images: [],
            createTime: '2026-03-30 10:00:00',
            orderNo: '20260328001'
          }
        ]
      } catch (error) {
        this.error = error.message
        console.error('获取服务提供者评价列表失败', error)
      } finally {
        this.isLoading = false
      }
    },
    
    // 获取评价详情
    async getReviewDetail(id) {
      this.isLoading = true
      this.error = null
      try {
        // 这里应该从后端获取评价详情
        // 暂时使用模拟数据
        this.currentReview = {
          id: id,
          userAvatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%2C%20simple%20style&image_size=square',
          userName: '王先生',
          rating: 5,
          content: '服务非常好，阿姨很专业，对我家狗狗很有耐心，下次还会预约！',
          images: [
            'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=happy%20dog%2C%20cute&image_size=square',
            'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=dog%20walking%2C%20outdoor&image_size=square'
          ],
          createTime: '2026-04-01 16:30:00',
          serviceName: '宠物陪伴',
          serviceTime: '2026-04-01 14:00-16:00',
          orderNo: '20260401001'
        }
      } catch (error) {
        this.error = error.message
        console.error('获取评价详情失败', error)
      } finally {
        this.isLoading = false
      }
    },
    
    // 创建评价
    async createReview(orderId, data) {
      this.isLoading = true
      this.error = null
      try {
        // 这里应该调用后端API创建评价
        console.log('创建评价:', orderId, data)
        // 模拟成功
        return true
      } catch (error) {
        this.error = error.message
        console.error('创建评价失败', error)
        throw error
      } finally {
        this.isLoading = false
      }
    }
  }
})

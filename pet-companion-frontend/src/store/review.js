import { defineStore } from 'pinia'
import api from '../api/axios' // ✅ 修复：直接从我们配置好的 axios 实例导入

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
    // ✅ 创建评价（对接后端）
    async createReview(orderId, data) {
      this.isLoading = true
      this.error = null
      try {
        const realOrderId = orderId?.value ?? orderId;

        // ✅ 发送请求：注意路径是否需要根据 baseURL 调整
        const response = await api.post('/review/create', {
          orderId: realOrderId,
          score: data.rating,
          content: data.content,
          images: Array.isArray(data.images) ? data.images.join(',') : data.images
        })

        // ✅ 核心调试：打印完整的响应对象
        console.log('=== 评价接口原始响应 ===')
        console.log('response:', response)
        console.log('response.data:', response.data)
        console.log('========================')

        // ✅ 修复：兼容不同的响应结构
        // 因为我们的 axios.js 拦截器返回的是 response，所以数据在 response.data 里
        const resData = response.data;

        if (resData && (resData.code === 200 || resData.status === 200)) {
          return true
        } else {
          console.error('响应码异常，实际为:', resData?.code)
          throw new Error(resData?.message || '评价失败')
        }
      } catch (error) {
        this.error = error.message
        console.error('创建评价失败', error)
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // ✅ 获取用户评价列表（对接后端）
    async getUserReviews() {
      this.isLoading = true
      try {
        // 1. 发送请求
        const response = await api.get('/review/user/list')

        // ✅ 核心调试：打印看看拿到的到底是什么结构
        console.log('=== 评价列表原始响应 ===')
        console.log('response:', response)
        console.log('response.data:', response.data)
        console.log('========================')

        // 2. 解析数据
        // 因为 axios.js 拦截器返回的是 response，所以业务数据在 response.data 里
        const resData = response.data;

        // 3. 判断状态码
        if (resData && resData.code === 200) {
          this.reviews = resData.data || []
          console.log('✅ 成功获取评价列表，数量:', this.reviews.length)
        } else {
          // 如果 code 不是 200，说明后端返回了业务错误
          console.error('❌ 响应码异常，实际为:', resData?.code)
          throw new Error(resData?.message || '获取评价列表失败')
        }
      } catch (error) {
        this.error = error.message
        console.error('取评价列表失败', error)
        throw error
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
    }
  }
})

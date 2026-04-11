import { defineStore } from 'pinia'
import axios from '../api/axios' // ✅ 必须确保这一行存在且路径正确


export const usePaymentStore = defineStore('payment', {
  state: () => ({
    payments: [],
    isLoading: false,
    error: null
  }),
  
  getters: {
    getPayments: (state) => state.payments
  },

  actions: {
    // ✅ 1. 获取真实的支付记录列表
    async getPaymentList() {
      this.isLoading = true
      this.error = null
      try {
        const response = await axios.get('/payment/list')
        if (response.data && response.data.code === 200) {
          this.payments = response.data.data
        }
      } catch (error) {
        this.error = error.message
        console.error('获取支付记录失败', error)
      } finally {
        this.isLoading = false
      }
    },

    // ✅ 2. 发起支付（真实场景：调用微信/支付宝接口）
    async createPayment(orderId, payType = 1) {
      this.isLoading = true
      this.error = null
      try {
        // 对应后端: POST /payment/create
        const response = await axios.post('/payment/create', {
          orderId: orderId,
          payType: payType // 1-微信, 2-支付宝
        })

        if (response.data && response.data.code === 200) {
          return response.data.data
        }
        throw new Error(response.data?.message || '创建支付单失败')
      } catch (error) {
        this.error = error.message
        console.error('发起支付失败', error)
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // ✅ 3. 模拟支付成功（开发测试专用）
    async mockPaySuccess(orderId) {
      this.isLoading = true
      this.error = null
      try {
        console.log(`正在模拟支付订单: ${orderId}`)
        // 对应后端: GET /payment/mock/{orderId}
        const response = await axios.get(`/payment/mock/${orderId}`)

        if (response.data && response.data.code === 200) {
          console.log('模拟支付成功:', response.data.message)
          return true
        }
        throw new Error(response.data?.message || '模拟支付失败')
      } catch (error) {
        this.error = error.message
        console.error('模拟支付异常', error)
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // ✅ 4. 退款
    async refund(orderId, amount) {
      this.isLoading = true
      this.error = null
      try {
        // TODO: 这里后续可以对接后端的退款接口
        console.log('发起退款:', orderId, amount)
        return {
          refundId: 'R' + Date.now(),
          status: 'SUCCESS'
        }
      } catch (error) {
        this.error = error.message
        console.error('发起退款失败', error)
        throw error
      } finally {
        this.isLoading = false
      }
    }
  }
})

import { defineStore } from 'pinia'
import api from '../api/index'

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
    // 获取支付记录列表
    async getPaymentList() {
      this.isLoading = true
      this.error = null
      try {
        // 这里应该从后端获取支付记录列表
        // 暂时使用模拟数据
        this.payments = [
          {
            id: 1,
            orderNo: '20260405001',
            amount: 100,
            paymentTime: '2026-04-05 10:30:00',
            status: 'SUCCESS',
            statusText: '支付成功',
            type: 'PAYMENT'
          },
          {
            id: 2,
            orderNo: '20260401001',
            amount: 40,
            paymentTime: '2026-04-01 14:00:00',
            status: 'SUCCESS',
            statusText: '支付成功',
            type: 'PAYMENT'
          },
          {
            id: 3,
            orderNo: '20260330001',
            amount: 30,
            paymentTime: '2026-03-30 09:00:00',
            status: 'SUCCESS',
            statusText: '退款成功',
            type: 'REFUND'
          }
        ]
      } catch (error) {
        this.error = error.message
        console.error('获取支付记录失败', error)
      } finally {
        this.isLoading = false
      }
    },
    
    // 发起支付
    async createPayment(orderId, amount) {
      this.isLoading = true
      this.error = null
      try {
        // 这里应该调用后端API发起支付
        console.log('发起支付:', orderId, amount)
        // 模拟成功
        return {
          paymentId: 'P' + Date.now(),
          status: 'SUCCESS'
        }
      } catch (error) {
        this.error = error.message
        console.error('发起支付失败', error)
        throw error
      } finally {
        this.isLoading = false
      }
    },
    
    // 退款
    async refund(orderId, amount) {
      this.isLoading = true
      this.error = null
      try {
        // 这里应该调用后端API发起退款
        console.log('发起退款:', orderId, amount)
        // 模拟成功
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

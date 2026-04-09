import { defineStore } from 'pinia'
import api from '../api'

export const useOrderStore = defineStore('order', {
  state: () => ({
    orders: [],
    currentOrder: null,
    pendingOrders: []
  }),
  
  getters: {
    getOrders: (state) => state.orders,
    getCurrentOrder: (state) => state.currentOrder,
    getPendingOrders: (state) => state.pendingOrders
  },
  
  actions: {
    // 获取订单列表
    async getOrderList() {
      try {
        const response = await api.get('/order')
        this.orders = response.data.data
        return response.data.data
      } catch (error) {
        throw error
      }
    },
    
    // 获取订单详情
    async getOrderDetail(id) {
      try {
        const response = await api.get(`/order/${id}`)
        this.currentOrder = response.data.data
        return response.data.data
      } catch (error) {
        throw error
      }
    },
    
    // 创建订单
    async createOrder(orderData) {
      try {
        const response = await api.post('/order', orderData)
        this.orders.unshift(response.data.data)
        return response.data.data
      } catch (error) {
        throw error
      }
    },
    
    // 取消订单
    async cancelOrder(id) {
      try {
        const response = await api.put(`/order/${id}/cancel`)
        const index = this.orders.findIndex(order => order.id === id)
        if (index !== -1) {
          this.orders[index] = response.data.data
        }
        if (this.currentOrder && this.currentOrder.id === id) {
          this.currentOrder = response.data.data
        }
        return response.data.data
      } catch (error) {
        throw error
      }
    },
    
    // 接受订单
    async acceptOrder(id) {
      try {
        const response = await api.put(`/order/${id}/accept`)
        const index = this.pendingOrders.findIndex(order => order.id === id)
        if (index !== -1) {
          this.pendingOrders.splice(index, 1)
        }
        if (this.currentOrder && this.currentOrder.id === id) {
          this.currentOrder = response.data.data
        }
        return response.data.data
      } catch (error) {
        throw error
      }
    },
    
    // 开始服务
    async startOrder(id) {
      try {
        const response = await api.put(`/order/${id}/start`)
        const index = this.orders.findIndex(order => order.id === id)
        if (index !== -1) {
          this.orders[index] = response.data.data
        }
        if (this.currentOrder && this.currentOrder.id === id) {
          this.currentOrder = response.data.data
        }
        return response.data.data
      } catch (error) {
        throw error
      }
    },
    
    // 完成服务
    async completeOrder(id) {
      try {
        const response = await api.put(`/order/${id}/complete`)
        const index = this.orders.findIndex(order => order.id === id)
        if (index !== -1) {
          this.orders[index] = response.data.data
        }
        if (this.currentOrder && this.currentOrder.id === id) {
          this.currentOrder = response.data.data
        }
        return response.data.data
      } catch (error) {
        throw error
      }
    },
    
    // 获取服务提供者订单列表
    async getProviderOrderList() {
      try {
        const response = await api.get('/order/provider')
        this.orders = response.data.data
        return response.data.data
      } catch (error) {
        throw error
      }
    },
    
    // 获取待接单订单列表
    async getPendingOrderList() {
      try {
        const response = await api.get('/order/pending')
        this.pendingOrders = response.data.data
        return response.data.data
      } catch (error) {
        throw error
      }
    }
  }
})

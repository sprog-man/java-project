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
    // ✅ 获取订单列表（对接后端）
    async getOrderList() {
      try {
        // 对应后端 OrderController 的 @GetMapping("/orderInfo")
        const response = await api.get('/order')
        if (response.data && response.data.code === 200) {
          this.orders = response.data.data
          return response.data.data
        }
        return []
      } catch (error) {
        console.error('Store: 获取订单列表失败', error)
        throw error
      }
    },

    // ✅ 新增：计算订单金额的方法
    calculateOrderPrice(servicePrice, startTime, endTime) {
      if (!startTime || !endTime) return { basePrice: 0, serviceFee: 0, totalPrice: 0, hours: 0 }

      // 使用同一个基准日期
      const baseDate = '2000-01-01'
      const start = new Date(`${baseDate} ${startTime}`)
      let end = new Date(`${baseDate} ${endTime}`)

      // ✅ 核心修复：如果结束时间小于开始时间，说明跨天了，给结束时间 +1 天
      if (end < start) {
        end.setDate(end.getDate() + 1)
      }

      let hours = (end - start) / (1000 * 60 * 60)

      // 防止其他极端情况导致的负数
      if (hours < 0) hours = 0

      const basePrice = servicePrice * hours
      const serviceFee = basePrice * 0.05 // 5% 服务费
      const totalPrice = basePrice + serviceFee

      return {
        basePrice: Number(basePrice.toFixed(2)),
        serviceFee: Number(serviceFee.toFixed(2)),
        totalPrice: Number(totalPrice.toFixed(2)),
        hours: Number(hours.toFixed(2))
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

    // ✅ 创建订单（对接后端标准 RESTful 接口）
    async createOrder(orderData) {
      try {
        // 对应后端 @PostMapping (即 /order)
        console.log('Store: 正在发送请求到 /order', orderData)
        const response = await api.post('/order', orderData)
        console.log('Store: 收到后端响应', response.data)

        if (response.data && response.data.code === 200) {
          return response.data.data
        }
        return response.data
      } catch (error) {
        console.error('Store: 创建订单 API 调用失败', error)
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
        // ✅ 确保返回的是后端最新的完整订单对象
        const updatedOrder = response.data.data;

        // 从待接单列表中移除（因为状态变了，它不再属于“待接单”了）
        const index = this.pendingOrders.findIndex(order => order.id === id)
        if (index !== -1) {
          this.pendingOrders.splice(index, 1)
        }

        // 如果当前详情页正好是这个订单，也更新一下
        if (this.currentOrder && this.currentOrder.id === id) {
          this.currentOrder = updatedOrder
        }

        return updatedOrder;
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

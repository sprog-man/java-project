import { defineStore } from 'pinia'
import axios from '../api/axios'

export const useServerStore = defineStore('server', {
    state: () => ({
        completedOrdersCount: 0,
        pendingOrdersCount: 0,
        totalEarnings: 0,
        averageRating: 4.8 // 给个默认值，防止 toFixed 报错
    }),

    actions: {
        // 获取已完成订单数
        async fetchCompletedOrdersCount() {
            try {
                const response = await axios.get('/service-provider/stats/completed-orders')
                if (response.data.code === 200) {
                    this.completedOrdersCount = response.data.data
                }
            } catch (error) {
                console.error('获取已完成订单数失败:', error)
            }
        },

        // ✅ 统一在这里获取所有统计数据
        async fetchAllStats() {
            try {
                console.log('正在从后端获取服务者统计数据...')
                // 对应后端: GET /order/provider/stats
                const response = await axios.get('/order/provider/stats')

                if (response.data && response.data.code === 200) {
                    const data = response.data.data
                    console.log('后端返回的统计数据:', data)

                    this.pendingOrdersCount = data.pendingOrders || 0
                    this.completedOrdersCount = data.completedOrders || 0
                    this.averageRating = data.averageRating || 4.8
                    this.totalEarnings = data.totalEarnings || 0
                } else {
                    console.error('获取统计数据失败:', response.data?.message)
                }
            } catch (error) {
                console.error('请求统计数据异常:', error)
            }
        }
    }
})

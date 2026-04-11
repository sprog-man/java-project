import { defineStore } from 'pinia'
import axios from '../api/axios'

export const useServerStore = defineStore('server', {
    state: () => ({
        completedOrdersCount: 0,
        pendingOrdersCount: 0,
        totalEarnings: 0,
        averageRating: 0
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

        // 你可以在这里继续添加获取其他统计数据的方法
        async fetchAllStats() {
            await this.fetchCompletedOrdersCount()
            // 待扩展：await this.fetchPendingOrdersCount()
        }
    }
})

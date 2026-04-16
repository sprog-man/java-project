<template>
  <div class="provider-order-detail">
    <Header />
    <div class="container">
      <button class="back-button" @click="$router.back()">← 返回列表</button>

      <div v-if="orderStore.isLoading" class="loading">加载中...</div>

      <div v-else-if="order" class="detail-card">
        <h2 class="page-title">订单详情</h2>

        <div class="info-section">
          <div class="info-row">
            <span class="label">订单号：</span>
            <span class="value">{{ order.orderNo }}</span>
          </div>
          <div class="info-row">
            <span class="label">订单状态：</span>
            <span class="value status-badge" :class="`status-${order.status}`">{{ order.statusText }}</span>
          </div>
          <div class="info-row">
            <span class="label">服务项目：</span>
            <span class="value">{{ order.serviceName }}</span>
          </div>
          <div class="info-row">
            <span class="label">宠物名称：</span>
            <span class="value">{{ order.petName }}</span>
          </div>
          <div class="info-row">
            <span class="label">服务时间：</span>
            <span class="value">{{ order.serviceTime }}</span>
          </div>
          <div class="info-row">
            <span class="label">服务地址：</span>
            <span class="value">{{ order.address }}</span>
          </div>
          <div class="info-row">
            <span class="label">联系电话：</span>
            <span class="value">{{ order.phone }}</span>
          </div>
          <div class="info-row">
            <span class="label">客户备注：</span>
            <span class="value">{{ order.notes || '无' }}</span>
          </div>
          <div class="info-row price-row">
            <span class="label">订单金额：</span>
            <span class="value price">¥{{ Number(order.price).toFixed(2) }}</span>
          </div>
        </div>

        <div class="action-buttons">
          <router-link :to="`/provider/orders`" class="btn-outline">返回列表</router-link>
          <!-- 这里可以根据 order.status 放置“开始服务”或“完成订单”按钮 -->
        </div>
      </div>
    </div>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useOrderStore } from '../../store/order'

const route = useRoute()
const orderStore = useOrderStore()

// 从 store 中获取当前订单
const order = computed(() => orderStore.currentOrder)

onMounted(async () => {
  const orderId = route.params.id
  console.log('详情页挂载，准备获取订单 ID:', orderId)
  if (orderId) {
    try {
      await orderStore.fetchOrderDetail(orderId)
      console.log('订单数据已更新:', orderStore.currentOrder)
    } catch (error) {
      // ✅ 增强报错提示，显示具体的错误信息
      const msg = error.response?.data?.message || error.message || '未知错误'
      alert(`获取订单信息失败: ${msg}`)
      console.error('详细错误对象:', error)
    }
  }
})
</script>

<style scoped>
/* 简单的样式补充 */
.detail-card {
  background: white;
  padding: 2rem;
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-sm);
  margin-top: 1rem;
}
.info-row {
  display: flex;
  margin-bottom: 1rem;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.5rem;
}
.label { width: 100px; color: #666; }
.value { flex: 1; font-weight: 500; }
.price { color: var(--cta-color); font-size: 1.2rem; }
</style>

<template>
  <div class="order-hall">
    <Header />

    <div class="container">
      <!-- 返回按钮 -->
      <div class="back-button-container">
        <button class="back-button" @click="navigateBack">
          ← 返回仪表盘
        </button>
      </div>
      
      <h2 class="page-title">订单大厅</h2>
      <p class="page-subtitle">查看并接取新订单</p>

      <!-- ✅ 增加加载状态提示 -->
      <div v-if="isLoading" class="loading-tip">正在为您寻找新订单...</div>

      <div class="order-card" v-for="order in pendingOrders" :key="order.id">
        <div class="order-header">
          <div class="order-no">订单号：{{ order.orderNo }}</div>
          <div class="order-status status-PENDING">{{ order.statusText }}</div>
        </div>

        <div class="order-content">
          <div class="service-info">
            <h3>{{ order.serviceName }} - {{ order.petName }}</h3>
            <p class="service-time">⏰ 服务时间：{{ order.serviceTime }}</p>
            <p class="service-address">📍 服务地址：{{ order.address }}</p>
            <p class="contact-info">📞 联系电话：{{ order.phone }}</p>
            <p v-if="order.notes" class="notes">📝 备注：{{ order.notes }}</p>
          </div>
          <div class="order-price">¥{{ Number(order.price).toFixed(2) }}</div>
        </div>

        <div class="order-actions">
          <router-link :to="`/provider/orders/${order.id}`" class="btn-outline">查看详情</router-link>
          <button class="btn-primary" @click="handleAcceptOrder(order.id)">接取订单</button>
        </div>
      </div>

      <div v-if="!isLoading && pendingOrders.length === 0" class="empty-order">
        <p>暂无新订单，请稍后再来刷新看看～</p>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script setup>import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useOrderStore } from '../../store/order'

const router = useRouter()
const orderStore = useOrderStore()
const isLoading = ref(false)

// 返回仪表盘
const navigateBack = () => {
  router.push('/server/dashboard')
}

// ✅ 使用计算属性从 Store 获取待接单订单
const pendingOrders = computed(() => orderStore.pendingOrders || [])

// 接取订单
const handleAcceptOrder = async (id) => {
  if (!confirm('确定要接取这个订单吗？')) return

  try {
    isLoading.value = true
    // ✅ 1. 调用后端接口，后端会将状态改为 3 (ACCEPTED)
    const result = await orderStore.acceptOrder(id)

    alert(`接单成功！当前订单状态已变更为：${result.statusText}`)

    // ✅ 2. 关键：重新从后端拉取最新的“待接单”列表
    // 这样刚才接的那个单（状态已是 ACCEPTED）就会自动从大厅消失
    await fetchPendingOrders()

    // ✅ 3. 可选：跳转到我的订单页查看刚接的单
    // router.push('/provider/orders')
  } catch (error) {
    console.error('接单失败', error)
    alert('接单失败：' + (error.message || '请重试'))
  } finally {
    isLoading.value = false
  }
}

// 获取待接单订单列表
const fetchPendingOrders = async () => {
  isLoading.value = true
  try {
    await orderStore.getPendingOrderList()
  } catch (error) {
    console.error('获取订单大厅数据失败', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchPendingOrders()
})
</script>

<style scoped>
.order-hall {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.back-button-container {
  margin-top: var(--spacing-lg);
  margin-bottom: var(--spacing-md);
}

.back-button {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-xs);
  padding: var(--spacing-xs) var(--spacing-md);
  background-color: transparent;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-md);
  color: var(--text-color);
  font-size: 0.875rem;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.back-button:hover {
  border-color: var(--cta-color);
  color: var(--cta-color);
  transform: translateX(-2px);
}

.page-title {
  font-size: 2rem;
  margin-top: var(--spacing-lg);
  margin-bottom: var(--spacing-sm);
  color: var(--primary-color);
  text-align: center;
}

.page-subtitle {
  text-align: center;
  margin-bottom: var(--spacing-xl);
  color: var(--light-text-color);
}

.order-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--spacing-lg);
  transition: all var(--transition-fast);
}

.order-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-5px);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
  padding-bottom: var(--spacing-md);
  border-bottom: 1px solid var(--border-color);
}

.order-no,
.order-time {
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.order-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--spacing-md);
  gap: var(--spacing-lg);
}

.service-info {
  flex: 1;
}

.service-info h3 {
  margin-bottom: var(--spacing-xs);
  color: var(--primary-color);
}

.service-time,
.service-address,
.contact-info {
  font-size: 0.875rem;
  color: var(--light-text-color);
  margin-bottom: var(--spacing-xs);
}

.order-price {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--cta-color);
  flex-shrink: 0;
}

.order-actions {
  display: flex;
  gap: var(--spacing-sm);
  justify-content: flex-end;
}

.order-actions .btn-primary,
.order-actions .btn-outline {
  padding: var(--spacing-xs) var(--spacing-md);
  font-size: 0.875rem;
  transition: all var(--transition-fast);
}

.order-actions .btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.empty-order {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-2xl);
  box-shadow: var(--shadow-sm);
  text-align: center;
  margin-top: var(--spacing-xl);
}

.empty-order p {
  color: var(--light-text-color);
  font-size: 1.125rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
  }
  
  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-xs);
  }
  
  .order-content {
    flex-direction: column;
    gap: var(--spacing-sm);
  }
  
  .order-actions {
    flex-direction: column;
  }
  
  .order-actions .btn-primary,
  .order-actions .btn-outline {
    width: 100%;
  }
}
</style>
<template>
  <div class="order-management">
    <Header />
    
    <div class="container">
      <!-- 返回按钮 -->
      <div class="back-button-container">
        <button class="back-button" @click="navigateBack">
          ← 返回仪表盘
        </button>
      </div>
      
      <h2 class="page-title">我的订单</h2>
      <p class="page-subtitle">管理您已接取的服务任务</p>

      <!-- 订单状态筛选 -->
      <div class="filter-section">
        <button
            v-for="status in orderStatuses"
            :key="status.value"
            :class="['filter-btn', { active: selectedStatus === status.value }]"
            @click="selectedStatus = status.value"
        >
          {{ status.label }}
        </button>
      </div>

      <!-- 订单列表 -->
      <div class="order-card" v-for="order in filteredOrders" :key="order.id">
        <div class="order-header">
          <div class="order-no">订单号：{{ order.orderNo }}</div>
          <div class="order-status" :class="`status-${order.status}`">{{ order.statusText }}</div>
        </div>

        <div class="order-content">
          <div class="service-info">
            <h3>{{ order.serviceName }} - {{ order.petName }}</h3>
            <p class="service-time">⏰ {{ order.serviceTime }}</p>
            <p class="service-address">📍 {{ order.address }}</p>
            <p class="contact-info">📞 {{ order.phone }}</p>
          </div>
          <div class="order-price">¥{{ Number(order.price).toFixed(2) }}</div>
        </div>

        <div class="order-actions">
          <router-link :to="`/provider/orders/${order.id}`" class="btn-outline">查看详情</router-link>

          <!-- ✅ 1. 已接单状态：显示【开始服务】 -->
          <button
              class="btn-primary"
              v-if="order.status === 'ACCEPTED'"
              @click="handleStartService(order.id)"
          >
            开始服务
          </button>

          <!-- ✅ 2. 服务中状态：显示【完成订单】 -->
          <button
              class="btn-success"
              v-if="order.status === 'IN_SERVICE'"
              @click="handleCompleteOrder(order.id)"
          >
            完成订单
          </button>
        </div>
      </div>

      <div v-if="!isLoading && filteredOrders.length === 0" class="empty-order">
        <p>暂无相关订单</p>
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
const selectedStatus = ref('all')
const isLoading = ref(false)

// 返回仪表盘
const navigateBack = () => {
  router.push('/server/dashboard')
}

// ✅ 更新状态选项以匹配后端返回的字符串
const orderStatuses = [
  { value: 'all', label: '全部' },
  { value: 'ACCEPTED', label: '已接单' },
  { value: 'IN_SERVICE', label: '服务中' },
  { value: 'COMPLETED', label: '已完成' }
]

const filteredOrders = computed(() => {
  const allOrders = orderStore.orders || []
  if (selectedStatus.value === 'all') {
    return allOrders
  }
  return allOrders.filter(order => order.status === selectedStatus.value)
})

// 开始服务
const handleStartService = async (id) => {
  if (!confirm('确定要开始这项服务吗？')) return
  try {
    await orderStore.startOrder(id)
    alert('服务已开始！请认真服务哦。')
    await fetchProviderOrders()
  } catch (error) {
    alert('操作失败：' + error.message)
  }
}

// 完成订单
const handleCompleteOrder = async (id) => {
  if (!confirm('确定要完成这个订单吗？完成后用户将进行评价。')) return
  try {
    await orderStore.completeOrder(id)
    alert('订单已完成！感谢您的付出。')
    await fetchProviderOrders()
  } catch (error) {
    alert('操作失败：' + error.message)
  }
}

const fetchProviderOrders = async () => {
  isLoading.value = true
  try {
    await orderStore.getProviderOrderList()
  } catch (error) {
    console.error('获取订单列表失败', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchProviderOrders()
})
</script>

<style scoped>
.order-management {
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

.filter-section {
  display: flex;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-lg);
  justify-content: center;
  flex-wrap: wrap;
}

.filter-btn {
  padding: var(--spacing-xs) var(--spacing-md);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-md);
  background-color: white;
  color: var(--text-color);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.filter-btn:hover {
  border-color: var(--cta-color);
  color: var(--cta-color);
}

.filter-btn.active {
  background-color: var(--cta-color);
  color: white;
  border-color: var(--cta-color);
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
  flex-wrap: wrap;
  gap: var(--spacing-sm);
}

.order-no,
.order-time {
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.status-badge {
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--border-radius-full);
  font-size: 0.75rem;
  font-weight: 500;
}

.status-badge.PENDING_SERVICE {
  background-color: #fff3cd;
  color: #856404;
}

.status-badge.IN_SERVICE {
  background-color: #cce7ff;
  color: #004085;
}

.status-badge.COMPLETED {
  background-color: #d4edda;
  color: #155724;
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
  
  .filter-section {
    justify-content: flex-start;
  }
}
</style>
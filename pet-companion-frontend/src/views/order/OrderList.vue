<template>
  <div class="order-list">
    <Header />
    
    <div class="container">
      <h2 class="page-title">我的订单</h2>
      
      <div class="order-filter">
        <button 
          v-for="status in orderStatuses" 
          :key="status.value"
          :class="['filter-button', { active: currentStatus === status.value }]"
          @click="currentStatus = status.value"
        >
          {{ status.label }}
        </button>
      </div>
      
      <div class="order-card" v-for="order in filteredOrders" :key="order.id">
        <div class="order-header">
          <div class="order-no">订单号：{{ order.orderNo }}</div>
          <div class="order-status" :class="`status-${order.status}`">{{ order.statusText }}</div>
        </div>

        <div class="order-content">
          <div class="service-info">
            <h3>{{ order.serviceName }}</h3>
            <p>{{ order.serviceTime }}</p>
          </div>
          <!-- ✅ 修改：使用 toFixed(2) 确保显示两位小数 -->
          <div class="order-price">¥{{ Number(order.price).toFixed(2) }}</div>
        </div>
        
        <div class="order-actions">
          <router-link :to="`/order/${order.id}`" class="btn-outline">查看详情</router-link>
          <button 
            v-if="order.status === 'PENDING' " 
            class="btn-primary" 
            @click="cancelOrder(order.id)"
          >
            取消订单
          </button>
          <button 
            v-if="order.status === 'PENDING_PAYMENT' " 
            class="btn-primary" 
            @click="payOrder(order.id)"
          >
            去支付
          </button>
          <button 
            v-if="order.status === 'COMPLETED' && !order.reviewed " 
            class="btn-primary" 
            @click="reviewOrder(order.id)"
          >
            去评价
          </button>
        </div>
      </div>
      
      <div v-if="filteredOrders.length === 0" class="empty-order">
        <p>暂无订单</p>
        <router-link to="/order/create" class="btn-primary">立即预约</router-link>
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
const orders = ref([]) // ✅ 这里的 orders 将从 Store 同步过来
const currentStatus = ref('ALL')

// 筛选后的订单
const filteredOrders = computed(() => {
  if (currentStatus.value === 'ALL') {
    return orders.value
  }
  return orders.value.filter(order => order.status === currentStatus.value)
})

// 订单状态
const orderStatuses = [
  { value: 'ALL', label: '全部' },
  { value: 'PENDING_PAYMENT', label: '待支付' },
  { value: 'PENDING', label: '待接单' },
  { value: 'IN_SERVICE', label: '服务中' },
  { value: 'COMPLETED', label: '已完成' },
  { value: 'CANCELLED', label: '已取消' }
]

// 取消订单
const cancelOrder = async (id) => {
  if (confirm('确定要取消这个订单吗？')) {
    try {
      await orderStore.cancelOrder(id)
      await orderStore.getOrderList() // ✅ 重新刷新列表
    } catch (error) {
      console.error('取消订单失败', error)
    }
  }
}

// 支付订单
const payOrder = (id) => {
  // 跳转到支付页面
  router.push(`/order/${id}`)
}

// 评价订单
const reviewOrder = (id) => {
  // 跳转到评价页面
  router.push(`/review/create/${id}`)
}

onMounted(async () => {
  try {
    // ✅ 调用 Store 获取真实数据
    await orderStore.getOrderList()
    orders.value = orderStore.orders || []
    console.log('当前用户的真实订单:', orders.value)
  } catch (error) {
    console.error('获取订单列表失败', error)
  }
})
</script>

<style scoped>
.order-list {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.page-title {
  font-size: 2rem;
  margin-top: var(--spacing-xl);
  margin-bottom: var(--spacing-xl);
  color: var(--primary-color);
  text-align: center;
}

.order-filter {
  display: flex;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-lg);
  flex-wrap: wrap;
}

.filter-button {
  padding: var(--spacing-xs) var(--spacing-md);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-md);
  background-color: white;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.filter-button:hover {
  border-color: var(--cta-color);
  color: var(--cta-color);
}

.filter-button.active {
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
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
  padding-bottom: var(--spacing-md);
  border-bottom: 1px solid var(--border-color);
}

.order-no {
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.order-status {
  font-size: 0.875rem;
  font-weight: 500;
  padding: 2px 8px;
  border-radius: var(--border-radius-sm);
}

.status-PENDING_PAYMENT {
  background-color: rgba(245, 158, 11, 0.1);
  color: var(--warning-color);
}

.status-PENDING {
  background-color: rgba(3, 105, 161, 0.1);
  color: var(--cta-color);
}

.status-IN_SERVICE {
  background-color: rgba(16, 185, 129, 0.1);
  color: var(--success-color);
}

.status-COMPLETED {
  background-color: rgba(16, 185, 129, 0.1);
  color: var(--success-color);
}

.status-CANCELLED {
  background-color: rgba(239, 68, 68, 0.1);
  color: var(--error-color);
}

.order-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
}

.service-info h3 {
  margin-bottom: var(--spacing-xs);
  color: var(--primary-color);
}

.service-info p {
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.order-price {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--cta-color);
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
  margin-bottom: var(--spacing-lg);
  color: var(--light-text-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
  }
  
  .order-filter {
    justify-content: center;
  }
  
  .order-content {
    flex-direction: column;
    align-items: flex-start;
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

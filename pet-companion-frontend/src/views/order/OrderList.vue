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
          <div class="order-price">¥{{ order.price }}</div>
        </div>
        
        <div class="order-actions">
          <router-link :to="`/user/orders/${order.id}`" class="btn-outline">查看详情</router-link>
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

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useOrderStore } from '../../store/order'

const router = useRouter()
const orderStore = useOrderStore()
const orders = ref([])
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
      // 重新获取订单列表
      await orderStore.getOrderList()
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
    // 这里应该从后端获取订单列表
    // 暂时使用模拟数据
    orders.value = [
      {
        id: 1,
        orderNo: '20260405001',
        serviceName: '宠物陪伴',
        serviceTime: '2026-04-10 14:00-16:00',
        price: 100,
        status: 'PENDING_PAYMENT',
        statusText: '待支付',
        reviewed: false
      },
      {
        id: 2,
        orderNo: '20260405002',
        serviceName: '宠物遛弯',
        serviceTime: '2026-04-08 09:00-10:00',
        price: 40,
        status: 'COMPLETED',
        statusText: '已完成',
        reviewed: false
      },
      {
        id: 3,
        orderNo: '20260405003',
        serviceName: '宠物喂食',
        serviceTime: '2026-04-05 12:00-12:30',
        price: 30,
        status: 'CANCELLED',
        statusText: '已取消',
        reviewed: false
      }
    ]
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

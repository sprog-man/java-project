<template>
  <div class="completed-orders">
    <Header />
    
    <div class="container">
      <!-- 返回按钮 -->
      <div class="back-button-container">
        <button class="back-button" @click="navigateBack">
          ← 返回仪表盘
        </button>
      </div>
      
      <h2 class="page-title">已完成订单</h2>
      
      <div class="order-card" v-for="order in completedOrders" :key="order.id">
        <div class="order-header">
          <div class="order-no">订单号：{{ order.orderNo }}</div>
          <div class="order-time">完成时间：{{ order.completeTime }}</div>
          <div class="status-badge completed">已完成</div>
        </div>
        
        <div class="order-content">
          <div class="service-info">
            <h3>{{ order.serviceName }}</h3>
            <p class="service-time">{{ order.serviceTime }}</p>
            <p class="service-address">{{ order.address }}</p>
            <p class="contact-info">联系人：{{ order.contactName }} {{ order.contactPhone }}</p>
          </div>
          <div class="order-price">¥{{ order.price }}</div>
        </div>
        
        <div class="order-actions">
          <router-link :to="`/provider/orders/${order.id}`" class="btn-outline">查看详情</router-link>
        </div>
      </div>
      
      <div v-if="completedOrders.length === 0" class="empty-order">
        <p>暂无已完成订单</p>
        <router-link to="/server/order-hall" class="btn-primary">去订单大厅接单</router-link>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useOrderStore } from '../../store/order'

const router = useRouter()
const orderStore = useOrderStore()
const completedOrders = ref([])

// 返回仪表盘
const navigateBack = () => {
  router.push('/server/dashboard')
}

onMounted(async () => {
  try {
    // 获取订单列表
    await orderStore.getOrderList()
    // 筛选已完成的订单
    completedOrders.value = orderStore.orders.filter(order => order.status === 'COMPLETED')
    // 按完成时间排序，最新的在前
    completedOrders.value.sort((a, b) => {
      return new Date(b.completeTime || b.createTime) - new Date(a.completeTime || a.createTime)
    })
  } catch (error) {
    console.error('获取订单列表失败', error)
  }
})
</script>

<style scoped>
.completed-orders {
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
  margin-bottom: var(--spacing-xl);
  color: var(--primary-color);
  text-align: center;
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
  flex-wrap: wrap;
  gap: var(--spacing-sm);
}

.order-no {
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.order-time {
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.status-badge {
  font-size: 0.875rem;
  font-weight: 500;
  padding: 4px 12px;
  border-radius: var(--border-radius-sm);
}

.status-badge.completed {
  background-color: rgba(16, 185, 129, 0.1);
  color: var(--success-color);
}

.order-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
  flex-wrap: wrap;
  gap: var(--spacing-md);
}

.service-info h3 {
  margin-bottom: var(--spacing-xs);
  color: var(--primary-color);
}

.service-info p {
  font-size: 0.875rem;
  color: var(--light-text-color);
  margin-bottom: var(--spacing-xs);
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
  
  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-sm);
  }
  
  .order-content {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-sm);
  }
  
  .order-actions {
    flex-direction: column;
    width: 100%;
  }
  
  .order-actions .btn-outline {
    width: 100%;
  }
}
</style>
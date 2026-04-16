<template>
  <div class="pending-order-list">
    <Header />
    
    <div class="container">
      <h2 class="page-title">待处理订单</h2>
      
      <div class="order-card" v-for="order in orders" :key="order.id">
        <div class="order-header">
          <div class="order-no">订单号：{{ order.orderNo }}</div>
          <div class="order-time">创建时间：{{ order.createTime }}</div>
        </div>
        
        <div class="order-content">
          <div class="service-info">
            <h3>{{ order.serviceName }}</h3>
            <p>{{ order.serviceTime }}</p>
            <p>{{ order.address }}</p>
            <p>联系人：{{ order.contactName }} {{ order.contactPhone }}</p>
          </div>
          <div class="order-price">¥{{ order.price }}</div>
        </div>
        
        <div class="order-actions">
          <router-link :to="`/provider/orders/${order.id}`" class="btn-outline">查看详情</router-link>
          <button class="btn-primary" @click="processOrder(order.id)">处理订单</button>
        </div>
      </div>
      
      <div v-if="orders.length === 0" class="empty-order">
        <p>暂无待处理订单</p>
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
const orders = ref([])

// 处理订单
const processOrder = (id) => {
  // 跳转到订单详情页面进行处理
  router.push(`/provider/orders/${id}`)
}

onMounted(async () => {
  try {
    // 这里应该从后端获取待处理订单列表
    // 暂时使用模拟数据
    orders.value = [
      {
        id: 1,
        orderNo: '20260405001',
        serviceName: '宠物陪伴',
        serviceTime: '2026-04-10 14:00-16:00',
        address: '北京市朝阳区宠物大厦101室',
        contactName: '王先生',
        contactPhone: '138****1234',
        price: 100,
        createTime: '2026-04-05 10:00:00'
      },
      {
        id: 2,
        orderNo: '20260405002',
        serviceName: '宠物遛弯',
        serviceTime: '2026-04-08 09:00-10:00',
        address: '北京市海淀区宠物小区202室',
        contactName: '李女士',
        contactPhone: '139****5678',
        price: 40,
        createTime: '2026-04-05 09:30:00'
      }
    ]
  } catch (error) {
    console.error('获取待处理订单列表失败', error)
  }
})
</script>

<style scoped>
.pending-order-list {
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

.service-info p {
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

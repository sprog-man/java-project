<template>
  <div class="provider-order-list">
    <Header />
    
    <div class="container">
      <h2 class="page-title">服务订单</h2>
      
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
      
      <div class="order-card" v-for="order in orders" :key="order.id">
        <div class="order-header">
          <div class="order-no">订单号：{{ order.orderNo }}</div>
          <div class="order-status" :class="`status-${order.status}`">{{ order.statusText }}</div>
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
          <button 
            v-if="order.status === 'PENDING'" 
            class="btn-primary" 
            @click="acceptOrder(order.id)"
          >
            接受订单
          </button>
          <button 
            v-if="order.status === 'IN_SERVICE'" 
            class="btn-primary" 
            @click="completeOrder(order.id)"
          >
            完成服务
          </button>
        </div>
      </div>
      
      <div v-if="orders.length === 0" class="empty-order">
        <p>暂无订单</p>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useOrderStore } from '../../store/order'

const orderStore = useOrderStore()
const orders = ref([])
const currentStatus = ref('ALL')

// 订单状态
const orderStatuses = [
  { value: 'ALL', label: '全部' },
  { value: 'PENDING', label: '待接单' },
  { value: 'IN_SERVICE', label: '服务中' },
  { value: 'COMPLETED', label: '已完成' },
  { value: 'CANCELLED', label: '已取消' }
]

// 接受订单
const acceptOrder = async (id) => {
  try {
    await orderStore.acceptOrder(id)
    // 重新获取订单列表
    await orderStore.getProviderOrderList()
  } catch (error) {
    console.error('接受订单失败', error)
  }
}

// 完成订单
const completeOrder = async (id) => {
  try {
    await orderStore.completeOrder(id)
    // 重新获取订单列表
    await orderStore.getProviderOrderList()
  } catch (error) {
    console.error('完成订单失败', error)
  }
}

onMounted(async () => {
  try {
    // 这里应该从后端获取服务提供者订单列表
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
        status: 'PENDING',
        statusText: '待接单'
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
        status: 'IN_SERVICE',
        statusText: '服务中'
      },
      {
        id: 3,
        orderNo: '20260405003',
        serviceName: '宠物喂食',
        serviceTime: '2026-04-05 12:00-12:30',
        address: '北京市西城区宠物胡同303号',
        contactName: '张先生',
        contactPhone: '137****9012',
        price: 30,
        status: 'COMPLETED',
        statusText: '已完成'
      }
    ]
  } catch (error) {
    console.error('获取订单列表失败', error)
  }
})
</script>

<style scoped>
.provider-order-list {
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

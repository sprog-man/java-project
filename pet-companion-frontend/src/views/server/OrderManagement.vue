<template>
  <div class="order-management">
    <Header />
    
    <div class="container">
      <h2 class="page-title">订单管理</h2>
      <p class="page-subtitle">管理您接取的订单任务</p>
      
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
          <div class="order-time">创建时间：{{ order.createTime }}</div>
          <div :class="['status-badge', order.status]">
            {{ getStatusText(order.status) }}
          </div>
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
          <button 
            class="btn-primary" 
            v-if="order.status === 'IN_SERVICE'"
            @click="completeOrder(order.id)"
          >
            完成订单
          </button>
          <button 
            class="btn-primary" 
            v-else-if="order.status === 'PENDING_SERVICE'"
            @click="startService(order.id)"
          >
            开始服务
          </button>
        </div>
      </div>
      
      <div v-if="filteredOrders.length === 0" class="empty-order">
        <p>暂无订单</p>
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
const selectedStatus = ref('all')

// 订单状态选项
const orderStatuses = [
  { value: 'all', label: '全部' },
  { value: 'PENDING_SERVICE', label: '待服务' },
  { value: 'IN_SERVICE', label: '服务中' },
  { value: 'COMPLETED', label: '已完成' }
]

// 过滤后的订单列表
const filteredOrders = computed(() => {
  if (selectedStatus.value === 'all') {
    return orders.value
  }
  return orders.value.filter(order => order.status === selectedStatus.value)
})

// 获取订单状态文本
const getStatusText = (status) => {
  const statusMap = {
    'PENDING_SERVICE': '待服务',
    'IN_SERVICE': '服务中',
    'COMPLETED': '已完成'
  }
  return statusMap[status] || '未知'
}

// 开始服务
const startService = (id) => {
  // 这里应该调用后端API开始服务
  console.log('开始服务:', id)
  // 模拟更新订单状态
  const order = orders.value.find(o => o.id === id)
  if (order) {
    order.status = 'IN_SERVICE'
  }
  alert('服务已开始！')
}

// 完成订单
const completeOrder = (id) => {
  // 这里应该调用后端API完成订单
  console.log('完成订单:', id)
  // 模拟更新订单状态
  const order = orders.value.find(o => o.id === id)
  if (order) {
    order.status = 'COMPLETED'
  }
  alert('订单已完成！')
}

// 获取订单列表
const fetchOrders = async () => {
  try {
    // 这里应该从后端获取服务者接取的订单列表
    // 暂时使用模拟数据
    orders.value = [
      {
        id: 1,
        orderNo: '20260410001',
        serviceName: '宠物陪伴',
        serviceTime: '2026-04-11 14:00-16:00',
        address: '北京市朝阳区宠物大厦101室',
        contactName: '王先生',
        contactPhone: '138****1234',
        price: 100,
        status: 'PENDING_SERVICE',
        createTime: '2026-04-10 10:00:00'
      },
      {
        id: 2,
        orderNo: '20260410002',
        serviceName: '宠物遛弯',
        serviceTime: '2026-04-12 09:00-10:00',
        address: '北京市海淀区宠物小区202室',
        contactName: '李女士',
        contactPhone: '139****5678',
        price: 40,
        status: 'IN_SERVICE',
        createTime: '2026-04-10 09:30:00'
      },
      {
        id: 3,
        orderNo: '20260409001',
        serviceName: '宠物喂食',
        serviceTime: '2026-04-10 18:00-19:00',
        address: '北京市西城区宠物胡同303号',
        contactName: '赵先生',
        contactPhone: '137****9012',
        price: 30,
        status: 'COMPLETED',
        createTime: '2026-04-09 16:00:00'
      }
    ]
  } catch (error) {
    console.error('获取订单列表失败', error)
  }
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.order-management {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.page-title {
  font-size: 2rem;
  margin-top: var(--spacing-xl);
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
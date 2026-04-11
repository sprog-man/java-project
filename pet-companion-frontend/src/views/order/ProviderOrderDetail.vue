<template>
  <div class="provider-order-detail">
    <Header />
    
    <div class="container">
      <div class="order-info-card">
        <div class="order-header">
          <h2>订单详情</h2>
          <div class="order-status" :class="`status-${order.status}`">{{ order.statusText }}</div>
        </div>
        
        <div class="order-details">
          <div class="detail-section">
            <h3>订单信息</h3>
            <div class="detail-item">
              <span class="detail-label">订单号：</span>
              <span class="detail-value">{{ order.orderNo }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">创建时间：</span>
              <span class="detail-value">{{ order.createTime }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">服务时间：</span>
              <span class="detail-value">{{ order.serviceTime }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">服务地址：</span>
              <span class="detail-value">{{ order.address }}</span>
            </div>
          </div>
          
          <div class="detail-section">
            <h3>服务信息</h3>
            <div class="detail-item">
              <span class="detail-label">服务类型：</span>
              <span class="detail-value">{{ order.serviceName }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">宠物信息：</span>
              <span class="detail-value">{{ order.petName }} ({{ order.petBreed }})</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">备注：</span>
              <span class="detail-value">{{ order.notes || '无' }}</span>
            </div>
          </div>
          
          <div class="detail-section">
            <h3>联系信息</h3>
            <div class="detail-item">
              <span class="detail-label">联系人：</span>
              <span class="detail-value">{{ order.contactName }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">联系电话：</span>
              <span class="detail-value">{{ order.contactPhone }}</span>
            </div>
          </div>
          
          <div class="detail-section">
            <h3>支付信息</h3>
            <div class="detail-item">
              <span class="detail-label">订单金额：</span>
              <span class="detail-value price">¥{{ order.price }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">支付状态：</span>
              <span class="detail-value">{{ order.paymentStatusText }}</span>
            </div>
          </div>
        </div>
        
        <div class="order-actions">
          <button 
            v-if="order.status === 'PENDING'" 
            class="btn-primary" 
            @click="acceptOrder"
          >
            接受订单
          </button>
          <button 
            v-if="order.status === 'IN_SERVICE'" 
            class="btn-primary" 
            @click="completeOrder"
          >
            完成服务
          </button>
          <router-link to="/server/order-management" class="btn-outline">返回列表</router-link>
        </div>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useOrderStore } from '../../store/order'

const route = useRoute()
const router = useRouter()
const orderStore = useOrderStore()
const order = ref({
  id: '',
  orderNo: '',
  status: '',
  statusText: '',
  createTime: '',
  serviceTime: '',
  address: '',
  serviceName: '',
  petName: '',
  petBreed: '',
  notes: '',
  contactName: '',
  contactPhone: '',
  price: 0,
  paymentStatusText: ''
})

// 接受订单
const acceptOrder = async () => {
  try {
    await orderStore.acceptOrder(order.value.id)
    // 重新获取订单详情
    await orderStore.getProviderOrderDetail(order.value.id)
  } catch (error) {
    console.error('接受订单失败', error)
  }
}

// 完成订单
const completeOrder = async () => {
  try {
    await orderStore.completeOrder(order.value.id)
    // 重新获取订单详情
    await orderStore.getProviderOrderDetail(order.value.id)
  } catch (error) {
    console.error('完成订单失败', error)
  }
}

onMounted(async () => {
  const id = route.params.id
  try {
    // 这里应该从后端获取订单详情
    // 暂时使用模拟数据
    order.value = {
      id: id,
      orderNo: '20260405001',
      status: 'PENDING',
      statusText: '待接单',
      createTime: '2026-04-05 10:00:00',
      serviceTime: '2026-04-10 14:00-16:00',
      address: '北京市朝阳区宠物大厦101室',
      serviceName: '宠物陪伴',
      petName: '小白',
      petBreed: '萨摩耶',
      notes: '狗狗比较活泼，需要多陪它玩耍',
      contactName: '王先生',
      contactPhone: '138****1234',
      price: 100,
      paymentStatusText: '已支付'
    }
  } catch (error) {
    console.error('获取订单详情失败', error)
  }
})
</script>

<style scoped>
.provider-order-detail {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.order-info-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
  margin-top: var(--spacing-xl);
  margin-bottom: var(--spacing-2xl);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-xl);
  padding-bottom: var(--spacing-lg);
  border-bottom: 1px solid var(--border-color);
}

.order-header h2 {
  color: var(--primary-color);
}

.order-status {
  font-size: 0.875rem;
  font-weight: 500;
  padding: 4px 12px;
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

.order-details {
  margin-bottom: var(--spacing-xl);
}

.detail-section {
  margin-bottom: var(--spacing-lg);
}

.detail-section h3 {
  margin-bottom: var(--spacing-md);
  color: var(--primary-color);
  font-size: 1.125rem;
}

.detail-item {
  display: flex;
  margin-bottom: var(--spacing-sm);
}

.detail-label {
  width: 120px;
  font-weight: 500;
  color: var(--text-color);
  flex-shrink: 0;
}

.detail-value {
  color: var(--light-text-color);
  flex: 1;
}

.detail-value.price {
  font-weight: 600;
  color: var(--cta-color);
  font-size: 1.125rem;
}

.order-actions {
  display: flex;
  gap: var(--spacing-md);
  justify-content: center;
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-color);
}

.order-actions .btn-primary,
.order-actions .btn-outline {
  padding: var(--spacing-sm) var(--spacing-xl);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .order-info-card {
    padding: var(--spacing-lg);
  }
  
  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-sm);
  }
  
  .detail-item {
    flex-direction: column;
    gap: var(--spacing-xs);
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

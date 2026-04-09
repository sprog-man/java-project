<template>
  <div class="order-detail">
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
            <div class="detail-item">
              <span class="detail-label">联系电话：</span>
              <span class="detail-value">{{ order.phone }}</span>
            </div>
            <div class="detail-item" v-if="order.notes">
              <span class="detail-label">备注信息：</span>
              <span class="detail-value">{{ order.notes }}</span>
            </div>
          </div>
          
          <div class="detail-section">
            <h3>服务信息</h3>
            <div class="detail-item">
              <span class="detail-label">服务类型：</span>
              <span class="detail-value">{{ order.serviceName }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">服务提供者：</span>
              <span class="detail-value">{{ order.providerName || '待分配' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">宠物信息：</span>
              <span class="detail-value">{{ order.petName }}</span>
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
            <div class="detail-item" v-if="order.paymentTime">
              <span class="detail-label">支付时间：</span>
              <span class="detail-value">{{ order.paymentTime }}</span>
            </div>
          </div>
        </div>
        
        <div class="order-actions">
          <button 
            v-if="order.status === 'PENDING'" 
            class="btn-primary" 
            @click="cancelOrder"
          >
            取消订单
          </button>
          <button 
            v-if="order.status === 'PENDING_PAYMENT'" 
            class="btn-primary" 
            @click="payOrder"
          >
            去支付
          </button>
          <button 
            v-if="order.status === 'COMPLETED' && !order.reviewed" 
            class="btn-primary" 
            @click="reviewOrder"
          >
            去评价
          </button>
          <router-link to="/user/orders" class="btn-outline">返回列表</router-link>
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
  phone: '',
  notes: '',
  serviceName: '',
  providerName: '',
  petName: '',
  price: 0,
  paymentStatusText: '',
  paymentTime: '',
  reviewed: false
})

// 取消订单
const cancelOrder = async () => {
  if (confirm('确定要取消这个订单吗？')) {
    try {
      await orderStore.cancelOrder(order.value.id)
      // 重新获取订单详情
      await orderStore.getOrderDetail(order.value.id)
    } catch (error) {
      console.error('取消订单失败', error)
    }
  }
}

// 支付订单
const payOrder = () => {
  // 跳转到支付页面
  console.log('去支付')
}

// 评价订单
const reviewOrder = () => {
  // 跳转到评价页面
  router.push(`/review/create/${order.value.id}`)
}

onMounted(async () => {
  const id = route.params.id
  try {
    // 从后端获取订单详情
    const orderDetail = await orderStore.getOrderDetail(id)
    order.value = orderDetail
  } catch (error) {
    console.error('获取订单详情失败', error)
  }
})
</script>

<style scoped>
.order-detail {
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

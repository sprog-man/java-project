<template>
  <div class="payment-list">
    <Header />
    
    <div class="container">
      <h2 class="page-title">支付记录</h2>
      
      <div class="payment-card" v-for="payment in payments" :key="payment.id">
        <div class="payment-header">
          <div class="payment-date">{{ payment.paymentTime }}</div>
          <div class="payment-status" :class="`status-${payment.status}`">{{ payment.statusText }}</div>
        </div>
        
        <div class="payment-content">
          <div class="payment-info">
            <h3>订单支付</h3>
            <p>订单号：{{ payment.orderNo }}</p>
          </div>
          <div class="payment-amount" :class="{ refund: payment.type === 'REFUND' }">{{ payment.type === 'REFUND' ? '-¥' : '¥' }}{{ payment.amount }}</div>
        </div>
      </div>
      
      <div v-if="payments.length === 0" class="empty-payment">
        <p>暂无支付记录</p>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { usePaymentStore } from '../../store/payment'

const paymentStore = usePaymentStore()
const payments = ref([])

onMounted(async () => {
  try {
    // 这里应该从后端获取支付记录
    // 暂时使用模拟数据
    payments.value = [
      {
        id: 1,
        orderNo: '20260405001',
        amount: 100,
        paymentTime: '2026-04-05 10:30:00',
        status: 'SUCCESS',
        statusText: '支付成功',
        type: 'PAYMENT'
      },
      {
        id: 2,
        orderNo: '20260401001',
        amount: 40,
        paymentTime: '2026-04-01 14:00:00',
        status: 'SUCCESS',
        statusText: '支付成功',
        type: 'PAYMENT'
      },
      {
        id: 3,
        orderNo: '20260330001',
        amount: 30,
        paymentTime: '2026-03-30 09:00:00',
        status: 'SUCCESS',
        statusText: '退款成功',
        type: 'REFUND'
      }
    ]
  } catch (error) {
    console.error('获取支付记录失败', error)
  }
})
</script>

<style scoped>
.payment-list {
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

.payment-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--spacing-lg);
  transition: all var(--transition-fast);
}

.payment-card:hover {
  box-shadow: var(--shadow-md);
}

.payment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
  padding-bottom: var(--spacing-md);
  border-bottom: 1px solid var(--border-color);
}

.payment-date {
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.payment-status {
  font-size: 0.875rem;
  font-weight: 500;
  padding: 2px 8px;
  border-radius: var(--border-radius-sm);
}

.status-SUCCESS {
  background-color: rgba(16, 185, 129, 0.1);
  color: var(--success-color);
}

.status-FAILED {
  background-color: rgba(239, 68, 68, 0.1);
  color: var(--error-color);
}

.status-PENDING {
  background-color: rgba(245, 158, 11, 0.1);
  color: var(--warning-color);
}

.payment-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.payment-info h3 {
  margin-bottom: var(--spacing-xs);
  color: var(--primary-color);
}

.payment-info p {
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.payment-amount {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--cta-color);
}

.payment-amount.refund {
  color: var(--error-color);
}

.empty-payment {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-2xl);
  box-shadow: var(--shadow-sm);
  text-align: center;
  margin-top: var(--spacing-xl);
}

.empty-payment p {
  color: var(--light-text-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
  }
  
  .payment-content {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-sm);
  }
}
</style>

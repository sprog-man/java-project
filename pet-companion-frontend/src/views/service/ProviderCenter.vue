<template>
  <div class="provider-center">
    <Header />
    
    <div class="container">
      <h2 class="page-title">服务提供者中心</h2>
      
      <div class="provider-info-card">
        <div class="provider-avatar">
          <img :src="provider?.avatar || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=provider%20avatar%2C%20simple%20style&image_size=square'" alt="服务提供者头像" />
        </div>
        <div class="provider-info">
          <h3>{{ provider?.name || '服务提供者' }}</h3>
          <p>{{ provider?.phone || '未设置手机号' }}</p>
          <p>{{ provider?.email || '未设置邮箱' }}</p>
          <div class="provider-rating">
            <span class="rating">评分：{{ provider?.rating || 0 }}/5</span>
            <span class="order-count">已完成订单：{{ provider?.orderCount || 0 }}</span>
          </div>
          <button class="btn-outline" @click="editProviderInfo">编辑资料</button>
        </div>
      </div>
      
      <div class="provider-stats">
        <div class="stat-card">
          <div class="stat-number">{{ pendingOrders }}</div>
          <div class="stat-label">待接单</div>
          <router-link to="/provider/orders" class="stat-link">查看</router-link>
        </div>
        <div class="stat-card">
          <div class="stat-number">{{ inServiceOrders }}</div>
          <div class="stat-label">服务中</div>
          <router-link to="/provider/orders" class="stat-link">查看</router-link>
        </div>
        <div class="stat-card">
          <div class="stat-number">{{ completedOrders }}</div>
          <div class="stat-label">已完成</div>
          <router-link to="/provider/orders" class="stat-link">查看</router-link>
        </div>
      </div>
      
      <div class="provider-actions">
        <h3>快捷操作</h3>
        <div class="action-grid">
          <router-link to="/provider/orders" class="action-card">
            <div class="action-icon">
              <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=order%20list%20icon%2C%20simple%20style&image_size=square" alt="订单管理" />
            </div>
            <div class="action-label">订单管理</div>
          </router-link>
          <router-link to="/provider/reviews" class="action-card">
            <div class="action-icon">
              <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=reviews%20icon%2C%20simple%20style&image_size=square" alt="评价管理" />
            </div>
            <div class="action-label">评价管理</div>
          </router-link>
          <router-link to="/provider/services" class="action-card">
            <div class="action-icon">
              <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=services%20icon%2C%20simple%20style&image_size=square" alt="服务管理" />
            </div>
            <div class="action-label">服务管理</div>
          </router-link>
          <router-link to="/provider/earnings" class="action-card">
            <div class="action-icon">
              <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=earnings%20icon%2C%20simple%20style&image_size=square" alt="收益管理" />
            </div>
            <div class="action-label">收益管理</div>
          </router-link>
        </div>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useProviderStore } from '../../store/provider'

const providerStore = useProviderStore()
const provider = computed(() => providerStore.getProvider)

// 统计数据
const pendingOrders = ref(2)
const inServiceOrders = ref(1)
const completedOrders = ref(15)

// 编辑服务提供者信息
const editProviderInfo = () => {
  // 这里可以跳转到编辑服务提供者信息页面
  console.log('编辑服务提供者信息')
}

onMounted(async () => {
  try {
    await providerStore.getProviderInfo()
  } catch (error) {
    console.error('获取服务提供者信息失败', error)
  }
})
</script>

<style scoped>
.provider-center {
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

.provider-info-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--spacing-xl);
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

.provider-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.provider-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.provider-info h3 {
  margin-bottom: var(--spacing-xs);
  color: var(--primary-color);
}

.provider-info p {
  margin-bottom: var(--spacing-xs);
  color: var(--light-text-color);
}

.provider-rating {
  display: flex;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-sm);
}

.rating {
  color: var(--warning-color);
  font-weight: 500;
}

.order-count {
  color: var(--light-text-color);
}

.provider-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-xl);
}

.stat-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  text-align: center;
  transition: all var(--transition-fast);
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
}

.stat-number {
  font-size: 2rem;
  font-weight: 600;
  color: var(--cta-color);
  margin-bottom: var(--spacing-xs);
}

.stat-label {
  color: var(--light-text-color);
  margin-bottom: var(--spacing-sm);
}

.stat-link {
  color: var(--cta-color);
  font-size: 0.875rem;
  text-decoration: none;
  transition: color var(--transition-fast);
}

.stat-link:hover {
  color: var(--primary-color);
}

.provider-actions {
  margin-bottom: var(--spacing-2xl);
}

.provider-actions h3 {
  margin-bottom: var(--spacing-lg);
  color: var(--primary-color);
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-lg);
}

.action-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  text-align: center;
  transition: all var(--transition-fast);
  text-decoration: none;
  color: var(--text-color);
}

.action-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
  color: var(--cta-color);
}

.action-icon {
  width: 60px;
  height: 60px;
  margin: 0 auto var(--spacing-md);
  background-color: var(--background-color);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: var(--shadow-sm);
}

.action-icon img {
  width: 30px;
  height: 30px;
  object-fit: contain;
}

.action-label {
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
  }
  
  .provider-info-card {
    flex-direction: column;
    text-align: center;
  }
  
  .provider-stats,
  .action-grid {
    grid-template-columns: 1fr;
  }
}
</style>

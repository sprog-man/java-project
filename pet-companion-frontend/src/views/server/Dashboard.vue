<template>
  <div class="provider-dashboard">
    <!-- 头部导航 -->
    <Header />
    
    <!-- 仪表盘头部 -->
    <section class="dashboard-header">
      <div class="container">
        <div class="header-content">
          <div class="header-info">
            <h1>服务者仪表盘</h1>
            <p>欢迎回来，{{ user?.nickname || user?.username }}</p>
          </div>
          <div class="header-actions">
            <button class="btn-primary" @click="navigateToOrders">查看全部订单</button>
            <button class="btn-outline" @click="navigateToServices">管理服务</button>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 统计卡片 -->
    <section class="stats-section">
      <div class="container">
        <div class="stats-grid">
          <div class="stat-card" @click="navigateToOrderManagement" style="cursor: pointer;">
            <div class="stat-icon">📋</div>
            <div class="stat-info">
              <h3>订单管理</h3>
              <p class="stat-value">{{ stats.pendingOrders }}</p>
            </div>
          </div>
          <div class="stat-card" @click="navigateToOrders" style="cursor: pointer;">
            <div class="stat-icon">✅</div>
            <div class="stat-info">
              <h3>已完成订单</h3>
              <p class="stat-value">{{ stats.completedOrders }}</p>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">⭐</div>
            <div class="stat-info">
              <h3>平均评分</h3>
              <p class="stat-value">{{ stats.averageRating.toFixed(1) }}</p>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">💰</div>
            <div class="stat-info">
              <h3>总收入</h3>
              <p class="stat-value">¥{{ stats.totalEarnings.toFixed(2) }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>
    

    
    <!-- 订单大厅按钮 -->
    <section class="order-hall-section">
      <div class="container">
        <div class="order-hall-button-container">
          <h2 class="section-title">订单大厅</h2>
          <p class="section-subtitle">查看并接取新订单</p>
          <button class="btn-primary btn-large" @click="navigateToOrderHall">进入订单大厅</button>
        </div>
      </div>
    </section>
    
    <!-- 服务管理 -->
    <section class="services-section">
      <div class="container">
        <h2 class="section-title">我的服务</h2>
        <div class="service-grid">
          <div class="service-card" v-for="service in services" :key="service.id">
            <div class="service-icon">{{ service.icon }}</div>
            <h3>{{ service.name }}</h3>
            <p>{{ service.description }}</p>
            <div class="service-price">¥{{ service.price.toFixed(2) }}</div>
            <div class="service-actions">
              <button class="btn-sm btn-outline" @click="editService(service.id)">编辑</button>
              <button class="btn-sm btn-danger" @click="deleteService(service.id)">删除</button>
            </div>
          </div>
          <div class="service-card add-service-card">
            <div class="add-service-icon">+</div>
            <h3>添加服务</h3>
            <p>创建新的服务类型</p>
            <button class="btn-sm btn-primary" @click="addService">添加</button>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 底部信息 -->
    <Footer />
  </div>
</template>

<script setup>import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useUserStore } from '../../store/user'
import { useProviderStore } from '../../store/provider'
import { useServerStore } from '../../store/server' // ✅ 引入新的 store


const router = useRouter()
const userStore = useUserStore()
const providerStore = useProviderStore()
const serverStore = useServerStore() // ✅ 初始化 store

// 用户信息
const user = computed(() => userStore.getUser)

/// 统计数据 (使用 computed 关联到 store)
const stats = computed(() => ({
  pendingOrders: serverStore.pendingOrdersCount, // 暂时保留静态或后续扩展
  completedOrders: serverStore.completedOrdersCount, // ✅ 动态获取
  averageRating: serverStore.averageRating || 4.8,
  totalEarnings: serverStore.totalEarnings || 2350.50
}))



// 服务列表
const services = ref([
  {
    id: 1,
    name: '宠物陪伴',
    description: '专业的宠物陪伴服务，让您的爱宠不再孤单',
    price: 50.00,
    icon: '🏠'
  },
  {
    id: 2,
    name: '宠物遛弯',
    description: '专业的宠物遛弯服务，让您的爱宠保持健康活力',
    price: 30.00,
    icon: '🐶'
  },
  {
    id: 3,
    name: '宠物喂食',
    description: '定时定量为您的爱宠提供营养均衡的饮食',
    price: 20.00,
    icon: '🍖'
  }
])

// 导航到订单页面（默认显示已完成订单）
const navigateToOrders = () => {
  router.push('/provider/orders')
}

// 导航到订单管理页面
const navigateToOrderManagement = () => {
  router.push('/server/order-management')
}

// 导航到服务管理页面
const navigateToServices = () => {
  router.push('/provider/services')
}

// 导航到订单大厅页面
const navigateToOrderHall = () => {
  router.push('/server/order-hall')
}

// 编辑服务
const editService = (serviceId) => {
  // 实现编辑服务逻辑
  console.log('编辑服务:', serviceId)
}

// 删除服务
const deleteService = (serviceId) => {
  if (confirm('确定要删除这个服务吗？')) {
    // 实现删除服务逻辑
    console.log('删除服务:', serviceId)
  }
}

// 添加服务
const addService = () => {
  // 实现添加服务逻辑
  console.log('添加服务')
}

// 页面加载时的处理
onMounted(async () => {
  // 检查用户是否已登录
  if (!userStore.getIsLoggedIn) {
    router.push('/login')
    return
  }

  // 获取用户信息
  try {
    await userStore.getUserInfo()
    const user = userStore.getUser

    // 检查用户是否已认证
    if (user.verified !== 1) {
      alert('您需要先完成认证才能访问服务者页面')
      router.push('/provider/apply')
      return
    }

    // ✅ 获取统计数据
    await serverStore.fetchAllStats()

  } catch (error) {
    console.error('获取用户信息失败', error)
    alert('获取用户信息失败，请重新登录')
    router.push('/login')
  }
})
</script>

<style scoped>
.dashboard-header {
  background-color: var(--background-color);
  padding: var(--spacing-xl) 0;
  border-bottom: 1px solid var(--border-color);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-info h1 {
  font-size: 2rem;
  margin-bottom: var(--spacing-xs);
  color: var(--primary-color);
}

.header-info p {
  color: var(--light-text-color);
  margin: 0;
}

.header-actions {
  display: flex;
  gap: var(--spacing-md);
}

.stats-section {
  padding: var(--spacing-xl) 0;
  background-color: white;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-lg);
}

.stat-card {
  background-color: var(--background-color);
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-lg);
  display: flex;
  align-items: center;
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-fast);
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
}

.stat-icon {
  font-size: 2rem;
  margin-right: var(--spacing-md);
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: white;
  border-radius: 50%;
  box-shadow: var(--shadow-sm);
}

.stat-info h3 {
  margin: 0 0 var(--spacing-xs) 0;
  font-size: 0.875rem;
  color: var(--light-text-color);
  font-weight: 500;
}

.stat-value {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--primary-color);
}

.section-title {
  font-size: 1.5rem;
  text-align: center;
  margin-bottom: var(--spacing-lg);
  color: var(--primary-color);
}

.order-hall-section {
  padding: var(--spacing-xl) 0;
  background-color: var(--background-color);
}

.order-hall-button-container {
  text-align: center;
  padding: var(--spacing-2xl);
  background-color: white;
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-sm);
  max-width: 600px;
  margin: 0 auto;
}

.section-subtitle {
  text-align: center;
  margin-bottom: var(--spacing-lg);
  color: var(--light-text-color);
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.btn-large {
  font-size: 1.125rem;
  padding: var(--spacing-md) var(--spacing-xl);
  border-radius: var(--border-radius-lg);
  transition: all var(--transition-fast);
}

.btn-large:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.services-section {
  padding: var(--spacing-xl) 0;
  background-color: white;
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--spacing-lg);
}

.service-card {
  background-color: var(--background-color);
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-fast);
}

.service-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
}

.service-icon {
  font-size: 2rem;
  margin-bottom: var(--spacing-md);
}

.service-card h3 {
  margin-bottom: var(--spacing-sm);
  color: var(--primary-color);
}

.service-card p {
  margin-bottom: var(--spacing-md);
  color: var(--light-text-color);
}

.service-price {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--cta-color);
  margin-bottom: var(--spacing-md);
}

.service-actions {
  display: flex;
  gap: var(--spacing-sm);
}

.add-service-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  border: 2px dashed var(--border-color);
  background-color: transparent;
}

.add-service-icon {
  font-size: 3rem;
  color: var(--border-color);
  margin-bottom: var(--spacing-md);
}

.add-service-card h3 {
  color: var(--text-color);
}

.add-service-card p {
  color: var(--light-text-color);
  margin-bottom: var(--spacing-md);
}

.btn-sm {
  padding: var(--spacing-xs) var(--spacing-sm);
  font-size: 0.875rem;
  border-radius: var(--border-radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
  border: none;
  font-weight: 500;
}

.btn-sm.btn-primary {
  background-color: var(--cta-color);
  color: white;
}

.btn-sm.btn-primary:hover {
  background-color: var(--primary-color);
}

.btn-sm.btn-outline {
  background-color: transparent;
  border: 1px solid var(--border-color);
  color: var(--text-color);
}

.btn-sm.btn-outline:hover {
  border-color: var(--cta-color);
  color: var(--cta-color);
}

.btn-sm.btn-danger {
  background-color: #dc3545;
  color: white;
}

.btn-sm.btn-danger:hover {
  background-color: #c82333;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-md);
  }
  
  .header-actions {
    width: 100%;
    justify-content: space-between;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .service-grid {
    grid-template-columns: 1fr;
  }
  
  .order-table {
    font-size: 0.875rem;
  }
  
  .order-table th,
  .order-table td {
    padding: var(--spacing-sm);
  }
}
</style>
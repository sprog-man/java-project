<template>
  <div class="admin-container">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>宠物陪伴平台</h2>
        <p>管理员后台</p>
      </div>
      <nav class="sidebar-nav">
        <ul>
          <li>
            <router-link to="/admin/center" class="nav-item active">
              <span class="nav-icon">🏠</span>
              <span class="nav-text">首页</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/users" class="nav-item">
              <span class="nav-icon">👥</span>
              <span class="nav-text">用户管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/services" class="nav-item">
              <span class="nav-icon">🏪</span>
              <span class="nav-text">服务管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/orders" class="nav-item">
              <span class="nav-icon">📋</span>
              <span class="nav-text">订单管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/pets" class="nav-item">
              <span class="nav-icon">🐶</span>
              <span class="nav-text">宠物管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/reviews" class="nav-item">
              <span class="nav-icon">⭐</span>
              <span class="nav-text">评价管理</span>
            </router-link>
          </li>
          <li class="logout">
            <a href="#" @click.prevent="handleLogout" class="nav-item">
              <span class="nav-icon">🚪</span>
              <span class="nav-text">退出登录</span>
            </a>
          </li>
        </ul>
      </nav>
    </aside>
    
    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 顶部导航 -->
      <header class="top-nav">
        <div class="top-nav-left">
          <h3>后台管理系统</h3>
        </div>
        <div class="top-nav-right">
          <span class="user-info">欢迎，{{ userInfo?.nickname || userInfo?.username }}</span>
          <button class="logout-btn" @click="handleLogout">退出登录</button>
        </div>
      </header>
      
      <!-- 内容区 -->
      <div class="content">
        <div class="dashboard">
          <h2>仪表盘</h2>
          <div class="stats-grid">
            <div class="stat-card">
              <div class="stat-icon">👥</div>
              <div class="stat-info">
                <h4>用户总数</h4>
                <p class="stat-value">{{ stats.userCount || 0 }}</p>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">🏪</div>
              <div class="stat-info">
                <h4>服务类型</h4>
                <p class="stat-value">{{ stats.serviceCount || 0 }}</p>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">📋</div>
              <div class="stat-info">
                <h4>订单总数</h4>
                <p class="stat-value">{{ stats.orderCount || 0 }}</p>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">⭐</div>
              <div class="stat-info">
                <h4>评价总数</h4>
                <p class="stat-value">{{ stats.reviewCount || 0 }}</p>
              </div>
            </div>
          </div>
          
          <div class="recent-activity">
            <h3>最近活动</h3>
            <div class="activity-list">
              <div class="activity-item" v-for="(item, index) in recentActivities" :key="index">
                <span class="activity-time">{{ item.time }}</span>
                <span class="activity-content">{{ item.content }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo')))
const stats = ref({
  userCount: 0,
  serviceCount: 0,
  orderCount: 0,
  reviewCount: 0
})
const recentActivities = ref([
  { time: '2026-04-08 10:00', content: '用户张三注册了账号' },
  { time: '2026-04-08 09:30', content: '用户李四创建了订单' },
  { time: '2026-04-08 09:00', content: '服务提供者王五申请了服务' },
  { time: '2026-04-07 18:00', content: '用户赵六提交了评价' }
])

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  router.push('/admin/login')
}

onMounted(() => {
  // 模拟获取统计数据
  setTimeout(() => {
    stats.value = {
      userCount: 1234,
      serviceCount: 56,
      orderCount: 789,
      reviewCount: 456
    }
  }, 500)
})
</script>

<style scoped>
.admin-container {
  display: flex;
  min-height: 100vh;
  background-color: #f0f2f5;
}

/* 侧边栏 */
.sidebar {
  width: 240px;
  background-color: #1f2329;
  color: white;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #30363d;
}

.sidebar-header h2 {
  margin: 0 0 5px 0;
  font-size: 18px;
}

.sidebar-header p {
  margin: 0;
  font-size: 14px;
  color: #8b949e;
}

.sidebar-nav {
  flex: 1;
  padding: 20px 0;
}

.sidebar-nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-nav li {
  margin: 0;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: #8b949e;
  text-decoration: none;
  transition: all 0.3s;
}

.nav-item:hover {
  background-color: #282f38;
  color: white;
}

.nav-item.active {
  background-color: #238636;
  color: white;
}

.nav-icon {
  margin-right: 12px;
  font-size: 18px;
}

.nav-text {
  font-size: 14px;
}

.logout {
  margin-top: auto;
  margin-bottom: 20px;
}

/* 主内容区 */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 顶部导航 */
.top-nav {
  background-color: white;
  padding: 0 30px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.top-nav-left h3 {
  margin: 0;
  font-size: 16px;
  color: #24292f;
}

.top-nav-right {
  display: flex;
  align-items: center;
}

.user-info {
  margin-right: 20px;
  font-size: 14px;
  color: #57606a;
}

.logout-btn {
  background: none;
  border: 1px solid #d0d7de;
  border-radius: 6px;
  padding: 6px 12px;
  font-size: 14px;
  color: #24292f;
  cursor: pointer;
  transition: all 0.3s;
}

.logout-btn:hover {
  background-color: #f6f8fa;
  border-color: #d0d7de;
}

/* 内容区 */
.content {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
}

.dashboard h2 {
  margin-top: 0;
  margin-bottom: 30px;
  color: #24292f;
  font-size: 20px;
}

/* 统计卡片 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.stat-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  transition: transform 0.3s, box-shadow 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  font-size: 32px;
  margin-right: 16px;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f6f8fa;
  border-radius: 50%;
}

.stat-info {
  flex: 1;
}

.stat-info h4 {
  margin: 0 0 5px 0;
  font-size: 14px;
  color: #57606a;
}

.stat-value {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #24292f;
}

/* 最近活动 */
.recent-activity {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.recent-activity h3 {
  margin-top: 0;
  margin-bottom: 20px;
  font-size: 16px;
  color: #24292f;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.activity-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border-bottom: 1px solid #eaecef;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-time {
  font-size: 12px;
  color: #8b949e;
}

.activity-content {
  font-size: 14px;
  color: #24292f;
}
</style>
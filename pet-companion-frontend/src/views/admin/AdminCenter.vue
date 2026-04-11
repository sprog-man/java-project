<template>
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
</template>

<script setup>import { ref, onMounted } from 'vue'
import { useAdminStore } from '../../store/admin'  // ✅ 引入专门的 admin store

const adminStore = useAdminStore()

// 统计数据状态：确保这里的 key 和后端 AdminStatsResponse 的字段名一致
const stats = ref({
  userCount: 0,
  serviceCount: 0,
  orderCount: 0,
  reviewCount: 0
})





// 最近活动（暂时保留静态，后续可对接后端日志接口）
const recentActivities = ref([
  { time: '2026-04-08 10:00', content: '用户张三注册了账号' },
  { time: '2026-04-08 09:30', content: '用户李四创建了订单' }
])

// 获取统计数据的方法
const fetchStats = async () => {
  try {
    // ✅ 调用 store 中已更新路径的 action
    const data = await adminStore.fetchDashboardStats()
    if (data) {
      stats.value = data
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped>
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
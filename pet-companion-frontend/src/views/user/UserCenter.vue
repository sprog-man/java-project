<template>
  <div class="user-center">
    <Header />
    
    <div class="container">
      <h2 class="page-title">个人中心</h2>
      
      <div class="user-info-card">
        <div class="user-avatar">
          <img :src="user?.avatar || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%2C%20simple%20style&image_size=square'" alt="用户头像" />
        </div>
        <div class="user-info">
          <h3>{{ user?.username || '用户' }}</h3>
          <p>{{ user?.phone || '未设置手机号' }}</p>
          <p>{{ user?.email || '未设置邮箱' }}</p>
          <button class="btn-outline" @click="editUserInfo">编辑资料</button>
          <button class="change-password-btn" @click="changePassword">修改密码</button>
        </div>
      </div>
      
      <div class="user-stats">
        <div class="stat-card">
          <div class="stat-number">{{ petCount }}</div>
          <div class="stat-label">我的宠物</div>
          <router-link to="/user/pets" class="stat-link">查看</router-link>
        </div>
        <div class="stat-card">
          <div class="stat-number">{{ orderCount }}</div>
          <div class="stat-label">我的订单</div>
          <router-link to="/user/orders" class="stat-link">查看</router-link>
        </div>
        <div class="stat-card">
          <div class="stat-number">{{ reviewCount }}</div>
          <div class="stat-label">我的评价</div>
          <router-link to="/user/reviews" class="stat-link">查看</router-link>
        </div>
      </div>
      
      <div class="user-actions">
        <h3>快捷操作</h3>
        <div class="action-grid">
          <router-link to="/order/create" class="action-card">
            <div class="action-icon">
              <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=create%20order%20icon%2C%20simple%20style&image_size=square" alt="创建订单" />
            </div>
            <div class="action-label">预约服务</div>
          </router-link>
          <router-link to="/user/pets/add" class="action-card">
            <div class="action-icon">
              <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=add%20pet%20icon%2C%20simple%20style&image_size=square" alt="添加宠物" />
            </div>
            <div class="action-label">添加宠物</div>
          </router-link>
          <router-link to="/provider/apply" class="action-card">
            <div class="action-icon">
              <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=provider%20apply%20icon%2C%20simple%20style&image_size=square" alt="申请服务提供者" />
            </div>
            <div class="action-label">申请成为服务提供者</div>
          </router-link>
          <router-link to="/user/payments" class="action-card">
            <div class="action-icon">
              <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=payment%20icon%2C%20simple%20style&image_size=square" alt="支付记录" />
            </div>
            <div class="action-label">支付记录</div>
          </router-link>
        </div>
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
import { useUserStore } from '../../store/user'
import { usePetStore } from '../../store/pet'
import axios from '../../api/axios'

const userStore = useUserStore()
const petStore = usePetStore()
const router = useRouter()
const user = computed(() => userStore.getUser)

// 统计数据
const petCount = ref(0)
const orderCount = ref(0)
const reviewCount = ref(0)

// 编辑用户信息
const editUserInfo = () => {
  router.push('/user/edit')
}
// 修改密码
const changePassword = () => {
  router.push('/user/change-password')
}

// 获取统计数据
const loadStatistics = async () => {
  try {
    console.log('开始加载统计数据')

    // 1. 获取宠物数量 (假设 getPetList 返回的是数组或者包含数组的对象)
    const petList = await petStore.getPetList()
    // 兼容处理：如果 getPetList 内部已经处理了 response.data，这里直接取长度
    petCount.value = Array.isArray(petList) ? petList.length : 0

    // 2. 获取订单数量
        const orderResponse = await axios.get('/order')
        // 修正：从 response.data 中获取业务数据
        const orderResult = orderResponse.data
        if (orderResult && orderResult.code === 200) {
          const orderData = orderResult.data || []
          orderCount.value = Array.isArray(orderData) ? orderData.length : 0
        }

    // 3. 获取评价数量
    const reviewResponse = await axios.get('/review/user/list')
    const reviewResult = reviewResponse.data
    if (reviewResult && reviewResult.code === 200) {
      const reviewData = reviewResult.data || []
      reviewCount.value = Array.isArray(reviewData) ? reviewData.length : 0
    }
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
}

onMounted(async () => {
  try {
    await userStore.getUserInfo()
  } catch (error) {
    console.error('获取用户信息失败', error)
  } finally {
    // 无论获取用户信息是否成功，都获取统计数据
    await loadStatistics()
  }
})
</script>

// ... existing code ...
<style scoped>
.user-center {
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

.user-info-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--spacing-xl);
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

.user-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-info h3 {
  margin-bottom: var(--spacing-xs);
  color: var(--primary-color);
}

.user-info p {
  margin-bottom: var(--spacing-xs);
  color: var(--light-text-color);
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.user-info button {
  margin-top: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--border-radius);
  font-size: 0.875rem;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.btn-outline {
  background-color: transparent;
  border: 1px solid var(--primary-color);
  color: var(--primary-color);
}

.btn-outline:hover {
  background-color: var(--primary-color);
  color: white;
}

.change-password-btn {
  background-color: transparent;
  border: 1px solid var(--cta-color);
  color: var(--cta-color);
}

.change-password-btn:hover {
  background-color: var(--cta-color);
  color: white;
}

.user-stats {
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

.user-actions {
  margin-bottom: var(--spacing-2xl);
}

.user-actions h3 {
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

  .user-info-card {
    flex-direction: column;
    text-align: center;
  }

  .user-stats,
  .action-grid {
    grid-template-columns: 1fr;
  }
}
</style>


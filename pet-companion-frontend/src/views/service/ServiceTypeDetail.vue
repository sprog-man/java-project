<template>
  <div class="service-type-detail">
    <Header />

    <div class="container">
      <!-- 增加一个加载判断，防止数据没回来时页面空白 -->
      <div v-if="serviceType.id" class="service-type-info">
        <div class="service-type-header">
          <div class="service-type-icon">
            <!-- 如果后端有 icon 字段就用后端的，没有就显示默认图标 -->
            <span style="font-size: 50px;">🐾</span>
          </div>
          <div class="service-type-title">
            <h2>{{ serviceType.name }}</h2>
            <div class="service-type-price">¥{{ Number(serviceType.price).toFixed(2) }}/小时</div>
          </div>
        </div>

        <div class="service-type-description">
          <h3>服务介绍</h3>
          <p>{{ serviceType.description }}</p>

          <!-- 暂时注释掉 features，因为数据库表里没有这个字段 -->
          <!--
          <ul class="service-type-features">
            <li v-for="feature in serviceType.features" :key="feature">{{ feature }}</li>
          </ul>
          -->
        </div>

        <div class="service-type-actions">
          <router-link to="/order/create" class="btn-primary">立即预约</router-link>
          <router-link to="/service/type" class="btn-outline">返回列表</router-link>
        </div>
      </div>
      <div v-else class="loading-state">
        <p>正在加载服务详情...</p>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>import { ref, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useServiceStore } from '../../store/service'

const route = useRoute()
const serviceStore = useServiceStore()

// ✅ 使用 computed 关联到 store 中的当前服务类型
const serviceType = computed(() => serviceStore.currentServiceType || {})

// ✅ 定义一个加载函数
const loadDetail = async (id) => {
  if (!id) {
    console.warn('详情页: ID 为空，无法加载');
    return
  }
  console.log('详情页: 准备加载 ID =', id);
  try {
    await serviceStore.fetchServiceTypeDetail(id)
  } catch (error) {
    console.error('详情页: 加载异常', error)
  }
}

onMounted(() => {
  // 页面挂载时加载数据
  loadDetail(route.params.id)
})

// ✅ 监听路由参数变化（防止从 A 服务直接跳到 B 服务时数据不更新）
watch(() => route.params.id, (newId) => {
  if (newId) loadDetail(newId)
})

</script>

<style scoped>
.service-type-detail {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.service-type-info {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
  margin-top: var(--spacing-xl);
  margin-bottom: var(--spacing-2xl);
}

.service-type-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-xl);
  padding-bottom: var(--spacing-lg);
  border-bottom: 1px solid var(--border-color);
}

.service-type-icon {
  width: 100px;
  height: 100px;
  background-color: var(--background-color);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: var(--shadow-sm);
}

.service-type-icon img {
  width: 50px;
  height: 50px;
  object-fit: contain;
}

.service-type-title h2 {
  margin-bottom: var(--spacing-sm);
  color: var(--primary-color);
}

.service-type-price {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--cta-color);
}

.service-type-description {
  margin-bottom: var(--spacing-xl);
}

.service-type-description h3 {
  margin-bottom: var(--spacing-md);
  color: var(--primary-color);
}

.service-type-description p {
  margin-bottom: var(--spacing-md);
  color: var(--light-text-color);
  line-height: 1.6;
}

.service-type-features {
  list-style: none;
  padding-left: var(--spacing-md);
}

.service-type-features li {
  position: relative;
  margin-bottom: var(--spacing-sm);
  color: var(--light-text-color);
}

.service-type-features li::before {
  content: '•';
  position: absolute;
  left: -16px;
  color: var(--cta-color);
  font-weight: bold;
}

.service-type-actions {
  display: flex;
  gap: var(--spacing-md);
  justify-content: center;
}

.service-type-actions .btn-primary,
.service-type-actions .btn-outline {
  padding: var(--spacing-sm) var(--spacing-xl);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .service-type-info {
    padding: var(--spacing-lg);
  }
  
  .service-type-header {
    flex-direction: column;
    text-align: center;
  }
  
  .service-type-actions {
    flex-direction: column;
  }
  
  .service-type-actions .btn-primary,
  .service-type-actions .btn-outline {
    width: 100%;
  }
}
</style>

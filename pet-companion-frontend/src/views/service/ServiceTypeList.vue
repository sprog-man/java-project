<template>
  <div class="service-type-list">
    <Header />
    
    <div class="container">
      <h2 class="page-title">服务类型</h2>
      <p class="page-subtitle">选择适合您爱宠的服务</p>
      
      <div class="service-type-grid">
        <div class="service-type-card" v-for="service in serviceTypes" :key="service.id">
          <div class="service-type-icon">
            <!-- 暂时使用默认图标，如果后端返回 icon 字段会自动切换 -->
            <span style="font-size: 40px;">🐾</span>
          </div>
          <h3>{{ service.name }}</h3>
          <p>{{ service.description }}</p>
          <div class="service-type-price">
            <span class="price">¥{{ Number(service.price).toFixed(2) }}/小时</span>
          </div>
          <router-link :to="`/service/type/${service.id}`" class="btn-primary">了解详情</router-link>
        </div>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>import { ref, onMounted, computed } from 'vue'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useServiceStore } from '../../store/service'

const serviceStore = useServiceStore()

// ✅ 使用 computed 关联到 store 中的数据
const serviceTypes = computed(() => serviceStore.serviceTypes || [])


onMounted(async () => {
  try {
    // 如果 store 里已经有数据，可以跳过请求，提高加载速度
    if (serviceTypes.value.length === 0) {
      await serviceStore.fetchServiceTypes()
    }
  } catch (error) {
    console.error('获取服务类型列表失败', error)
  }
})
</script>

<style scoped>
.service-type-list {
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

.service-type-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-2xl);
}

.service-type-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-fast);
  text-align: center;
}

.service-type-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
}

.service-type-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto var(--spacing-md);
  background-color: var(--background-color);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: var(--shadow-sm);
}

.service-type-icon img {
  width: 40px;
  height: 40px;
  object-fit: contain;
}

.service-type-card h3 {
  margin-bottom: var(--spacing-sm);
  color: var(--primary-color);
}

.service-type-card p {
  margin-bottom: var(--spacing-md);
  color: var(--light-text-color);
}

.service-type-price {
  margin-bottom: var(--spacing-md);
}

.price {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--cta-color);
}

.service-type-card .btn-primary {
  width: 100%;
  padding: var(--spacing-sm) 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
  }
  
  .service-type-grid {
    grid-template-columns: 1fr;
  }
}
</style>

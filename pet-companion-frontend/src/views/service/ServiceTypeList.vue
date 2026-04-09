<template>
  <div class="service-type-list">
    <Header />
    
    <div class="container">
      <h2 class="page-title">服务类型</h2>
      <p class="page-subtitle">选择适合您爱宠的服务</p>
      
      <div class="service-type-grid">
        <div class="service-type-card" v-for="service in serviceTypes" :key="service.id">
          <div class="service-type-icon">
            <img :src="service.icon" :alt="service.name" />
          </div>
          <h3>{{ service.name }}</h3>
          <p>{{ service.description }}</p>
          <div class="service-type-price">
            <span class="price">¥{{ service.price }}/小时</span>
          </div>
          <router-link :to="`/service/type/${service.id}`" class="btn-primary">了解详情</router-link>
        </div>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useServiceStore } from '../../store/service'

const serviceStore = useServiceStore()
const serviceTypes = ref([])

onMounted(async () => {
  try {
    // 这里应该从后端获取服务类型列表
    // 暂时使用模拟数据
    serviceTypes.value = [
      {
        id: 1,
        name: '宠物陪伴',
        description: '专业的宠物陪伴服务，让您的爱宠不再孤单',
        price: 50,
        icon: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pet%20companion%20icon%2C%20friendly%20style%2C%20simple%20design&image_size=square'
      },
      {
        id: 2,
        name: '宠物喂食',
        description: '定时定量为您的爱宠提供营养均衡的饮食',
        price: 30,
        icon: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pet%20feeding%20icon%2C%20friendly%20style%2C%20simple%20design&image_size=square'
      },
      {
        id: 3,
        name: '宠物遛弯',
        description: '专业的宠物遛弯服务，让您的爱宠保持健康活力',
        price: 40,
        icon: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pet%20walking%20icon%2C%20friendly%20style%2C%20simple%20design&image_size=square'
      },
      {
        id: 4,
        name: '宠物清洁',
        description: '专业的宠物清洁服务，让您的爱宠保持干净整洁',
        price: 60,
        icon: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pet%20grooming%20icon%2C%20friendly%20style%2C%20simple%20design&image_size=square'
      }
    ]
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

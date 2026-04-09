<template>
  <div class="service-type-detail">
    <Header />
    
    <div class="container">
      <div class="service-type-info">
        <div class="service-type-header">
          <div class="service-type-icon">
            <img :src="serviceType.icon" :alt="serviceType.name" />
          </div>
          <div class="service-type-title">
            <h2>{{ serviceType.name }}</h2>
            <div class="service-type-price">¥{{ serviceType.price }}/小时</div>
          </div>
        </div>
        
        <div class="service-type-description">
          <h3>服务介绍</h3>
          <p>{{ serviceType.description }}</p>
          <ul class="service-type-features">
            <li v-for="feature in serviceType.features" :key="feature">{{ feature }}</li>
          </ul>
        </div>
        
        <div class="service-type-actions">
          <router-link to="/order/create" class="btn-primary">立即预约</router-link>
          <router-link to="/service/type" class="btn-outline">返回列表</router-link>
        </div>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useServiceStore } from '../../store/service'

const route = useRoute()
const serviceStore = useServiceStore()
const serviceType = ref({
  id: '',
  name: '',
  description: '',
  price: 0,
  features: [],
  icon: ''
})

onMounted(async () => {
  const id = route.params.id
  try {
    // 这里应该从后端获取服务类型详情
    // 暂时使用模拟数据
    serviceType.value = {
      id: id,
      name: '宠物陪伴',
      description: '专业的宠物陪伴服务，让您的爱宠不再孤单。我们的服务提供者经过专业培训，能够为您的爱宠提供贴心的照顾和陪伴，包括玩耍、互动、安抚等。',
      price: 50,
      features: [
        '专业的宠物陪伴服务',
        '经过培训的服务提供者',
        '贴心的照顾和陪伴',
        '定期反馈宠物状态',
        '灵活的服务时间'
      ],
      icon: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pet%20companion%20icon%2C%20friendly%20style%2C%20simple%20design&image_size=square'
    }
  } catch (error) {
    console.error('获取服务类型详情失败', error)
  }
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

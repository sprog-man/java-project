<template>
  <div class="home">
    <!-- 头部导航 -->
    <Header />
    
    <!-- 轮播图 -->
    <section class="hero">
      <div class="hero-content">
        <div class="hero-text">
          <h1>专业的同城上门宠物陪伴服务</h1>
          <p>为您的爱宠提供贴心的照顾和陪伴，让您的宠物不再孤单</p>
          <div class="hero-buttons">
            <router-link to="/order/create" class="btn-primary">立即预约</router-link>
            <router-link to="/service/type" class="btn-outline">了解服务</router-link>
            <button @click="handleProviderPage" class="btn-outline">服务者页面</button>
          </div>
        </div>
        <div class="hero-image">
          <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20pet%20companion%20service%20illustration%2C%20professional%20pet%20care%2C%20warm%20colors%2C%20friendly%20style&image_size=landscape_16_9" alt="宠物陪伴服务" />
        </div>
      </div>
    </section>
    
    <!-- 服务类型 -->
    <section class="services">
      <div class="container">
        <h2 class="section-title">我们的服务</h2>
        <p class="section-subtitle">为您的爱宠提供全方位的陪伴和照顾</p>
        
        <div class="service-grid">
          <div class="service-card" v-for="service in services" :key="service.id">
            <div class="service-icon">
              <img :src="service.icon" :alt="service.name" />
            </div>
            <h3>{{ service.name }}</h3>
            <p>{{ service.description }}</p>
            <router-link :to="`/service/type/${service.id}`" class="service-link">了解详情 →</router-link>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 推荐服务提供者 -->
    <section class="providers">
      <div class="container">
        <h2 class="section-title">推荐服务提供者</h2>
        <p class="section-subtitle">专业、可靠的宠物陪伴服务提供者</p>
        
        <div class="provider-grid">
          <div class="provider-card" v-for="provider in providers" :key="provider.id">
            <div class="provider-avatar">
              <img :src="provider.avatar" :alt="provider.name" />
            </div>
            <h3>{{ provider.name }}</h3>
            <div class="provider-rating">
              <span class="star">{{ provider.rating }}</span>
              <span class="rating">{{ provider.reviews }} 评价</span>
            </div>
            <p class="provider-description">{{ provider.description }}</p>
            <div class="provider-tags">
              <span class="tag tag-primary" v-for="tag in provider.tags" :key="tag">{{ tag }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 用户评价 -->
    <section class="reviews">
      <div class="container">
        <h2 class="section-title">用户评价</h2>
        <p class="section-subtitle">来自真实用户的反馈</p>
        
        <div class="review-grid">
          <div class="review-card" v-for="review in reviews" :key="review.id">
            <div class="review-header">
              <div class="review-user">
                <div class="user-avatar">
                  <img :src="review.userAvatar" :alt="review.userName" />
                </div>
                <div class="user-info">
                  <h4>{{ review.userName }}</h4>
                  <div class="review-rating">
                    <span class="star">{{ review.rating }}</span>
                  </div>
                </div>
              </div>
              <div class="review-date">{{ review.date }}</div>
            </div>
            <p class="review-content">{{ review.content }}</p>
            <div class="review-service">{{ review.serviceName }}</div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 底部信息 -->
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useUserStore } from '../../store/user'

// 服务类型数据
const services = ref([
  {
    id: 1,
    name: '宠物陪伴',
    description: '专业的宠物陪伴服务，让您的爱宠不再孤单',
    icon: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pet%20companion%20icon%2C%20friendly%20style%2C%20simple%20design&image_size=square'
  },
  {
    id: 2,
    name: '宠物喂食',
    description: '定时定量为您的爱宠提供营养均衡的饮食',
    icon: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pet%20feeding%20icon%2C%20friendly%20style%2C%20simple%20design&image_size=square'
  },
  {
    id: 3,
    name: '宠物遛弯',
    description: '专业的宠物遛弯服务，让您的爱宠保持健康活力',
    icon: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pet%20walking%20icon%2C%20friendly%20style%2C%20simple%20design&image_size=square'
  },
  {
    id: 4,
    name: '宠物清洁',
    description: '专业的宠物清洁服务，让您的爱宠保持干净整洁',
    icon: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pet%20grooming%20icon%2C%20friendly%20style%2C%20simple%20design&image_size=square'
  }
])

// 服务提供者数据
const providers = ref([
  {
    id: 1,
    name: '张阿姨',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=friendly%20middle-aged%20woman%20pet%20caregiver%2C%20professional%20looking%2C%20warm%20smile&image_size=square',
    rating: 4.9,
    reviews: 128,
    description: '有10年宠物护理经验，擅长照顾各种宠物',
    tags: ['经验丰富', '有耐心', '价格合理']
  },
  {
    id: 2,
    name: '李师傅',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=friendly%20middle-aged%20man%20pet%20caregiver%2C%20professional%20looking%2C%20warm%20smile&image_size=square',
    rating: 4.8,
    reviews: 96,
    description: '专业宠物训练师，擅长大型犬的照顾和训练',
    tags: ['专业训练', '大型犬专家', '有爱心']
  },
  {
    id: 3,
    name: '王小姐',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=friendly%20young%20woman%20pet%20caregiver%2C%20professional%20looking%2C%20warm%20smile&image_size=square',
    rating: 4.7,
    reviews: 72,
    description: '动物医学专业，擅长宠物健康护理和紧急处理',
    tags: ['专业医学背景', '细心', '有责任心']
  }
])

// 用户评价数据
const reviews = ref([
  {
    id: 1,
    userName: '王先生',
    userAvatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=friendly%20young%20man%20avatar%2C%20simple%20style&image_size=square',
    rating: 5,
    date: '2026-03-15',
    content: '张阿姨非常专业，对我家狗狗很有耐心，狗狗很喜欢她。服务态度好，价格合理，会再次预约。',
    serviceName: '宠物陪伴'
  },
  {
    id: 2,
    userName: '李女士',
    userAvatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=friendly%20young%20woman%20avatar%2C%20simple%20style&image_size=square',
    rating: 4,
    date: '2026-03-10',
    content: '李师傅很专业，我家金毛很调皮，他都能很好地照顾，遛弯回来狗狗很开心。',
    serviceName: '宠物遛弯'
  },
  {
    id: 3,
    userName: '赵先生',
    userAvatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=friendly%20middle-aged%20man%20avatar%2C%20simple%20style&image_size=square',
    rating: 5,
    date: '2026-03-05',
    content: '王小姐很专业，我家猫咪生病的时候她能及时发现并处理，非常感谢她的细心照顾。',
    serviceName: '宠物陪伴'
  }
])

const router = useRouter()
const userStore = useUserStore()

// 处理服务者页面按钮点击
const handleProviderPage = async () => {
  // 检查用户是否登录
  if (!userStore.getIsLoggedIn) {
    router.push('/login')
    return
  }
  
  // 获取用户信息
  try {
    await userStore.getUserInfo()
    const user = userStore.getUser
    
    // 检查用户是否已认证
    if (user.verified === 1) {
      router.push('/server/dashboard')
    } else {
      alert('您需要先完成认证才能访问服务者页面')
      router.push('/provider/apply')
    }
  } catch (error) {
    console.error('获取用户信息失败', error)
    alert('获取用户信息失败，请重新登录')
    router.push('/login')
  }
}

// 页面加载时的处理
onMounted(() => {
  // 可以在这里添加一些初始化逻辑，比如从后端获取数据
})
</script>

<style scoped>
.hero {
  background-color: var(--background-color);
  padding: var(--spacing-2xl) 0;
  position: relative;
  overflow: hidden;
}

.hero::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 50%;
  height: 100%;
  background-color: rgba(3, 105, 161, 0.05);
  border-radius: 50% 0 0 50%;
  z-index: 0;
}

.hero-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--spacing-2xl);
  align-items: center;
  position: relative;
  z-index: 1;
}

.hero-text h1 {
  font-size: 2.5rem;
  margin-bottom: var(--spacing-md);
  color: var(--primary-color);
}

.hero-text p {
  font-size: 1.125rem;
  margin-bottom: var(--spacing-xl);
  color: var(--light-text-color);
}

.hero-buttons {
  display: flex;
  gap: var(--spacing-md);
}

.hero-image img {
  width: 100%;
  border-radius: var(--border-radius-xl);
  box-shadow: var(--shadow-lg);
}

.services {
  padding: var(--spacing-2xl) 0;
  background-color: white;
}

.section-title {
  font-size: 2rem;
  text-align: center;
  margin-bottom: var(--spacing-sm);
  color: var(--primary-color);
}

.section-subtitle {
  text-align: center;
  margin-bottom: var(--spacing-xl);
  color: var(--light-text-color);
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--spacing-lg);
}

.service-card {
  background-color: var(--background-color);
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-lg);
  text-align: center;
  transition: all var(--transition-fast);
  box-shadow: var(--shadow-sm);
}

.service-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
}

.service-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto var(--spacing-md);
  background-color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: var(--shadow-sm);
}

.service-icon img {
  width: 40px;
  height: 40px;
  object-fit: contain;
}

.service-card h3 {
  margin-bottom: var(--spacing-sm);
  color: var(--primary-color);
}

.service-card p {
  margin-bottom: var(--spacing-md);
  color: var(--light-text-color);
}

.service-link {
  display: inline-block;
  color: var(--cta-color);
  font-weight: 500;
  transition: all var(--transition-fast);
}

.service-link:hover {
  transform: translateX(5px);
}

.providers {
  padding: var(--spacing-2xl) 0;
  background-color: var(--background-color);
}

.provider-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--spacing-lg);
}

.provider-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-lg);
  transition: all var(--transition-fast);
  box-shadow: var(--shadow-sm);
}

.provider-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
}

.provider-avatar {
  width: 80px;
  height: 80px;
  margin: 0 auto var(--spacing-md);
  border-radius: 50%;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.provider-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.provider-card h3 {
  text-align: center;
  margin-bottom: var(--spacing-xs);
  color: var(--primary-color);
}

.provider-rating {
  text-align: center;
  margin-bottom: var(--spacing-md);
}

.star {
  color: var(--warning-color);
  font-weight: 600;
  margin-right: var(--spacing-xs);
}

.rating {
  color: var(--light-text-color);
  font-size: 0.875rem;
}

.provider-description {
  margin-bottom: var(--spacing-md);
  color: var(--light-text-color);
  text-align: center;
}

.provider-tags {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: var(--spacing-xs);
}

.reviews {
  padding: var(--spacing-2xl) 0;
  background-color: white;
}

.review-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--spacing-lg);
}

.review-card {
  background-color: var(--background-color);
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-lg);
  transition: all var(--transition-fast);
  box-shadow: var(--shadow-sm);
}

.review-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--spacing-md);
}

.review-user {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-info h4 {
  margin-bottom: var(--spacing-xs);
  color: var(--primary-color);
}

.review-date {
  color: var(--light-text-color);
  font-size: 0.875rem;
}

.review-content {
  margin-bottom: var(--spacing-md);
  color: var(--text-color);
  line-height: 1.6;
}

.review-service {
  color: var(--cta-color);
  font-size: 0.875rem;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-content {
    grid-template-columns: 1fr;
    text-align: center;
  }
  
  .hero-text h1 {
    font-size: 2rem;
  }
  
  .hero-buttons {
    justify-content: center;
  }
  
  .service-grid,
  .provider-grid,
  .review-grid {
    grid-template-columns: 1fr;
  }
  
  .section-title {
    font-size: 1.5rem;
  }
}
</style>

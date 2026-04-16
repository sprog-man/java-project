<template>
  <div class="review-detail">
    <Header />
    
    <div class="container">
      <div class="review-card">
        <div class="review-header">
          <div class="user-info">
            <div class="user-avatar">
              <img :src="review.userAvatar" :alt="review.userName" />
            </div>
            <div class="user-details">
              <div class="user-name">{{ review.userName }}</div>
              <div class="review-time">{{ review.createTime }}</div>
            </div>
          </div>
          <div class="review-rating">
            <span class="star" v-for="i in 5" :key="i" :class="{ active: i <= review.rating }">&#9733;</span>
          </div>
        </div>
        
        <div class="review-content">
          <h3>{{ review.serviceName }}</h3>
          <p>{{ review.content }}</p>
          <div class="review-images" v-if="review.images && review.images.length > 0">
            <img v-for="(image, index) in review.images" :key="index" :src="image" :alt="'评价图片' + (index + 1)" />
          </div>
        </div>
        
        <div class="review-footer">
          <div class="order-info">
            <span class="order-label">订单号：</span>
            <span class="order-no">{{ review.orderNo }}</span>
          </div>
          <div class="service-time">{{ review.serviceTime }}</div>
        </div>
      </div>
      
      <div class="action-buttons">
        <router-link to="/user/reviews" class="btn-outline">返回列表</router-link>
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
import { useReviewStore } from '../../store/review'

const route = useRoute()
const reviewStore = useReviewStore()
const review = ref({
  id: '',
  userAvatar: '',
  userName: '',
  rating: 0,
  content: '',
  images: [],
  createTime: '',
  serviceName: '',
  serviceTime: '',
  orderNo: ''
})

onMounted(async () => {
  const id = route.params.id
  try {
    // 这里应该从后端获取评价详情
    // 暂时使用模拟数据
    review.value = {
      id: id,
      userAvatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%2C%20simple%20style&image_size=square',
      userName: '王先生',
      rating: 5,
      content: '服务非常好，阿姨很专业，对我家狗狗很有耐心，下次还会预约！',
      images: [
        'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=happy%20dog%2C%20cute&image_size=square',
        'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=dog%20walking%2C%20outdoor&image_size=square'
      ],
      createTime: '2026-04-01 16:30:00',
      serviceName: '宠物陪伴',
      serviceTime: '2026-04-01 14:00-16:00',
      orderNo: '20260401001'
    }
  } catch (error) {
    console.error('获取评价详情失败', error)
  }
})
</script>

<style scoped>
.review-detail {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.review-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
  margin-top: var(--spacing-xl);
  margin-bottom: var(--spacing-lg);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--spacing-xl);
  padding-bottom: var(--spacing-lg);
  border-bottom: 1px solid var(--border-color);
}

.user-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.user-name {
  font-weight: 500;
  color: var(--text-color);
}

.review-time {
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.review-rating {
  display: flex;
  gap: 2px;
  flex-shrink: 0;
}

.star {
  color: var(--border-color);
  font-size: 1.25rem;
}

.star.active {
  color: var(--warning-color);
}

.review-content {
  margin-bottom: var(--spacing-xl);
}

.review-content h3 {
  margin-bottom: var(--spacing-md);
  color: var(--primary-color);
}

.review-content p {
  color: var(--text-color);
  line-height: 1.6;
  margin-bottom: var(--spacing-md);
}

.review-images {
  display: flex;
  gap: var(--spacing-sm);
  flex-wrap: wrap;
}

.review-images img {
  width: 120px;
  height: 120px;
  border-radius: var(--border-radius-md);
  object-fit: cover;
  box-shadow: var(--shadow-sm);
}

.review-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-color);
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.order-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
}

.order-label {
  font-weight: 500;
}

.action-buttons {
  display: flex;
  justify-content: center;
  margin-bottom: var(--spacing-2xl);
}

.action-buttons .btn-outline {
  padding: var(--spacing-sm) var(--spacing-xl);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .review-card {
    padding: var(--spacing-lg);
  }
  
  .review-header {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-sm);
  }
  
  .review-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-xs);
  }
  
  .review-images img {
    width: 100px;
    height: 100px;
  }
}
</style>

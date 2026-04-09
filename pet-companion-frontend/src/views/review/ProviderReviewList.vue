<template>
  <div class="provider-review-list">
    <Header />
    
    <div class="container">
      <h2 class="page-title">评价管理</h2>
      
      <div class="review-card" v-for="review in reviews" :key="review.id">
        <div class="review-header">
          <div class="user-info">
            <div class="user-avatar">
              <img :src="review.userAvatar" :alt="review.userName" />
            </div>
            <div class="user-name">{{ review.userName }}</div>
          </div>
          <div class="review-rating">
            <span class="star" v-for="i in 5" :key="i" :class="{ active: i <= review.rating }">&#9733;</span>
          </div>
        </div>
        
        <div class="review-content">
          <p>{{ review.content }}</p>
          <div class="review-images" v-if="review.images && review.images.length > 0">
            <img v-for="(image, index) in review.images" :key="index" :src="image" :alt="'评价图片' + (index + 1)" />
          </div>
        </div>
        
        <div class="review-footer">
          <div class="review-time">{{ review.createTime }}</div>
          <div class="review-order">订单号：{{ review.orderNo }}</div>
        </div>
      </div>
      
      <div v-if="reviews.length === 0" class="empty-review">
        <p>暂无评价</p>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useReviewStore } from '../../store/review'

const reviewStore = useReviewStore()
const reviews = ref([])

onMounted(async () => {
  try {
    // 这里应该从后端获取服务提供者评价列表
    // 暂时使用模拟数据
    reviews.value = [
      {
        id: 1,
        userAvatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%2C%20simple%20style&image_size=square',
        userName: '王先生',
        rating: 5,
        content: '服务非常好，阿姨很专业，对我家狗狗很有耐心，下次还会预约！',
        images: [
          'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=happy%20dog%2C%20cute&image_size=square'
        ],
        createTime: '2026-04-05 15:00:00',
        orderNo: '20260401001'
      },
      {
        id: 2,
        userAvatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%2C%20simple%20style&image_size=square',
        userName: '李女士',
        rating: 4,
        content: '服务不错，准时到达，狗狗很开心。',
        images: [],
        createTime: '2026-03-30 10:00:00',
        orderNo: '20260328001'
      },
      {
        id: 3,
        userAvatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%2C%20simple%20style&image_size=square',
        userName: '张先生',
        rating: 5,
        content: '非常专业，服务周到，值得推荐！',
        images: [
          'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=dog%20walking%2C%20outdoor&image_size=square'
        ],
        createTime: '2026-03-25 14:00:00',
        orderNo: '20260320001'
      }
    ]
  } catch (error) {
    console.error('获取评价列表失败', error)
  }
})
</script>

<style scoped>
.provider-review-list {
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

.review-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--spacing-lg);
  transition: all var(--transition-fast);
}

.review-card:hover {
  box-shadow: var(--shadow-md);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
}

.user-info {
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

.user-name {
  font-weight: 500;
  color: var(--text-color);
}

.review-rating {
  display: flex;
  gap: 2px;
}

.star {
  color: var(--border-color);
  font-size: 1rem;
}

.star.active {
  color: var(--warning-color);
}

.review-content {
  margin-bottom: var(--spacing-md);
}

.review-content p {
  color: var(--text-color);
  line-height: 1.6;
  margin-bottom: var(--spacing-sm);
}

.review-images {
  display: flex;
  gap: var(--spacing-sm);
  flex-wrap: wrap;
}

.review-images img {
  width: 80px;
  height: 80px;
  border-radius: var(--border-radius-md);
  object-fit: cover;
  box-shadow: var(--shadow-sm);
}

.review-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: var(--spacing-md);
  border-top: 1px solid var(--border-color);
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.empty-review {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-2xl);
  box-shadow: var(--shadow-sm);
  text-align: center;
  margin-top: var(--spacing-xl);
}

.empty-review p {
  color: var(--light-text-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
  }
  
  .review-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-xs);
  }
}
</style>

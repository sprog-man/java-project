<template>
  <div class="user-review-list">
    <Header />
    
    <div class="container">
      <h2 class="page-title">我的评价</h2>

      <!-- ✅ 循环渲染真实数据 -->
      <div class="review-card" v-for="review in reviews" :key="review.id">
        <div class="review-header">
          <div class="service-info">
            <!-- ✅ 显示动态获取的服务名称 -->
            <h3>{{ review.serviceName || '宠物陪伴服务' }}</h3>
            <!-- ✅ 显示订单号 -->
            <p>订单号：{{ review.orderNo }}</p>
            <!-- ✅ 显示服务时间 -->
            <p>服务时间：{{ review.serviceTime }}</p>
          </div>
          <div class="review-rating">
            <!-- ✅ 评分渲染：注意后端返回的是 score -->
            <span class="star" v-for="i in 5" :key="i" :class="{ active: i <= review.score }">&#9733;</span>
          </div>
        </div>

        <div class="review-content">
          <p>{{ review.content }}</p>
          <!-- ✅ 图片渲染：处理逗号分隔的字符串 -->
          <div class="review-images" v-if="review.images && review.images.length > 0">
            <img
                v-for="(image, index) in review.images.split(',')"
                :key="index"
                :src="image.trim()"
                :alt="'评价图片' + (index + 1)"
            />
          </div>
        </div>

        <div class="review-footer">
          <!-- ✅ 显示评价创建时间 -->
          <div class="review-time">评价于：{{ review.createTime }}</div>
        </div>
      </div>
      
      <div v-if="reviews.length === 0" class="empty-review">
        <p>暂无评价</p>
        <router-link to="/user/orders" class="btn-primary">查看订单</router-link>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>import { ref, computed, onMounted } from 'vue' // ✅ 修复：增加 computed 的导入
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useReviewStore } from '../../store/review'

const reviewStore = useReviewStore()

// ✅ 使用 computed 从 Store 获取数据，并处理图片格式
const reviews = computed(() => {
  return reviewStore.reviews.map(review => ({
    ...review,
    // 后端 images 是逗号分隔的字符串，前端需要转成数组
    images: review.images ? review.images.split(',') : []
  }))
})

onMounted(async () => {
  try {
    // ✅ 调用 Store 方法获取真实数据
    await reviewStore.getUserReviews()
  } catch (error) {
    console.error('加载评价列表出错', error)
  }
})
</script>

<style scoped>
.user-review-list {
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
  align-items: flex-start;
  margin-bottom: var(--spacing-md);
}

.service-info h3 {
  margin-bottom: var(--spacing-xs);
  color: var(--primary-color);
}

.service-info p {
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
  margin-bottom: var(--spacing-lg);
  color: var(--light-text-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
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
}
</style>

<template>
  <div class="create-review">
    <Header />
    
    <div class="container">
      <h2 class="page-title">评价服务</h2>

      <!-- ✅ 修复：增加 v-if 保护，确保 orderId 存在再渲染表单 -->
      <form v-if="orderId" class="create-review-form" @submit.prevent="handleSubmit">
        <div class="form-section">
          <h3>服务信息</h3>
          <div class="service-info">
            <p><strong>服务类型：</strong>{{ orderInfo.serviceName }}</p>
            <p><strong>服务时间：</strong>{{ orderInfo.serviceTime }}</p>
            <p><strong>服务提供者：</strong>{{ orderInfo.providerName }}</p>
          </div>
        </div>
        
        <div class="form-section">
          <h3>评分</h3>
          <div class="rating-container">
            <span 
              class="star" 
              v-for="i in 5" 
              :key="i" 
              :class="{ active: i <= reviewForm.rating }"
              @click="reviewForm.rating = i"
            >
              &#9733;
            </span>
            <span class="rating-text">{{ ratingText }}</span>
          </div>
        </div>
        
        <div class="form-section">
          <h3>评价内容</h3>
          <textarea 
            v-model="reviewForm.content" 
            class="form-input" 
            placeholder="请描述您的服务体验"
            rows="4"
            required
          ></textarea>
        </div>
        
        <div class="form-section">
          <h3>上传图片（可选）</h3>
          <input 
            type="file" 
            class="form-input" 
            accept="image/*"
            multiple
            @change="handleFileUpload"
          />
          <div class="image-preview" v-if="reviewForm.images && reviewForm.images.length > 0">
            <div class="preview-item" v-for="(image, index) in reviewForm.images" :key="index">
              <img :src="image" :alt="'评价图片' + (index + 1)" />
              <button type="button" class="remove-image" @click="removeImage(index)">&times;</button>
            </div>
          </div>
        </div>

        <div class="form-actions">
          <button type="submit" class="btn-primary" :disabled="isLoading">
            <span v-if="isLoading" class="loading"></span>
            <span v-else>提交评价</span>
          </button>
          <!-- ✅ 修复：使用 computed 后的 orderId -->
          <router-link :to="`/user/orders`" class="btn-outline">取消</router-link>
        </div>
      </form>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useReviewStore } from '../../store/review'
import { useOrderStore } from '../../store/order'

const route = useRoute()
const router = useRouter()
const reviewStore = useReviewStore()
const orderStore = useOrderStore()

// ✅ 修复：使用 ref 存储 ID，初始为 null
const orderId = ref(null)

// 订单信息
const orderInfo = ref({
  serviceName: '',
  serviceTime: '',
  providerName: ''
})

// 评价表单
const reviewForm = ref({
  rating: 5,
  content: '',
  images: []
})

// 加载状态
const isLoading = ref(false)

// 评分文本
const ratingText = computed(() => {
  const texts = ['非常差', '差', '一般', '好', '非常好']
  return texts[reviewForm.value.rating - 1]
})

// 处理文件上传
const handleFileUpload = (event) => {
  const files = event.target.files
  if (files && files.length > 0) {
    // 这里应该上传文件到服务器，暂时使用模拟的图片URL
    for (let i = 0; i < files.length; i++) {
      reviewForm.value.images.push('https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pet%20service%20review%20image&image_size=square')
    }
  }
}

// 移除图片
const removeImage = (index) => {
  reviewForm.value.images.splice(index, 1)
}

// 处理表单提交
const handleSubmit = async () => {
  isLoading.value = true
  try {
    // ✅ 修复：准备提交的数据
    const submitData = {
      rating: reviewForm.value.rating,
      content: reviewForm.value.content,
      // 如果没有图片，就传 null 或空字符串，不要传空数组
      images: reviewForm.value.images.length > 0 ? reviewForm.value.images.join(',') : ''
    }

    await reviewStore.createReview(orderId.value, submitData)

    alert('评价提交成功！')
    // ✅ 核心修复：提交成功后，主动刷新订单列表
    await orderStore.getOrderList()

    router.push('/user/orders')
  } catch (error) {
    console.error('提交评价失败', error)
    alert('提交失败：' + (error.message || '未知错误'))
  } finally {
    isLoading.value = false
  }
}

onMounted(async () => {
  // ✅ 修复：从 route.params.orderId 获取（对应路由定义里的 :orderId）
  const id = route.params.orderId;
  console.log('CreateReview.vue - 原始路由参数:', route.params);

  if (!id) {
    alert('无法获取订单ID，请从订单列表页点击“去评价”进入')
    router.back()
    return
  }

  orderId.value = id;
  console.log('CreateReview.vue - 成功获取订单 ID:', orderId.value);

  try {
    // 获取订单详情
    const order = await orderStore.getOrderDetail(orderId.value)
    if (order) {
      orderInfo.value = {
        serviceName: order.serviceName,
        serviceTime: order.serviceTime,
        providerName: order.providerName
      }
    } else {
      alert('无法获取订单信息')
      router.back()
    }
  } catch (error) {
    console.error('获取订单信息失败', error)
    alert('加载订单信息出错')
    router.back()
  }
})
</script>

<style scoped>
.create-review {
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

.create-review-form {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--spacing-2xl);
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
}

.form-section {
  margin-bottom: var(--spacing-xl);
  padding-bottom: var(--spacing-lg);
  border-bottom: 1px solid var(--border-color);
}

.form-section:last-child {
  border-bottom: none;
  margin-bottom: var(--spacing-lg);
  padding-bottom: 0;
}

.form-section h3 {
  margin-bottom: var(--spacing-lg);
  color: var(--primary-color);
  font-size: 1.125rem;
}

.service-info {
  background-color: var(--background-color);
  padding: var(--spacing-lg);
  border-radius: var(--border-radius-md);
}

.service-info p {
  margin-bottom: var(--spacing-sm);
  color: var(--text-color);
}

.rating-container {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.star {
  font-size: 2rem;
  color: var(--border-color);
  cursor: pointer;
  transition: color var(--transition-fast);
}

.star:hover,
.star.active {
  color: var(--warning-color);
}

.rating-text {
  font-size: 1.125rem;
  color: var(--text-color);
  font-weight: 500;
}

.form-input {
  width: 100%;
  padding: var(--spacing-sm) var(--spacing-md);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-md);
  font-size: 1rem;
  transition: border-color var(--transition-fast);
}

.form-input:focus {
  outline: none;
  border-color: var(--cta-color);
  box-shadow: 0 0 0 3px rgba(3, 105, 161, 0.1);
}

.image-preview {
  display: flex;
  gap: var(--spacing-sm);
  flex-wrap: wrap;
  margin-top: var(--spacing-sm);
}

.preview-item {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: var(--border-radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.preview-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-image {
  position: absolute;
  top: 5px;
  right: 5px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
  line-height: 1;
}

.form-actions {
  display: flex;
  gap: var(--spacing-md);
  justify-content: center;
  margin-top: var(--spacing-xl);
}

.form-actions .btn-primary,
.form-actions .btn-outline {
  padding: var(--spacing-sm) var(--spacing-xl);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
  }
  
  .create-review-form {
    padding: var(--spacing-lg);
    margin: 0 var(--spacing-md) var(--spacing-xl);
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .form-actions .btn-primary,
  .form-actions .btn-outline {
    width: 100%;
  }
}
</style>

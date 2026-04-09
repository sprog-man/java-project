<template>
  <div class="provider-apply">
    <Header />
    
    <div class="container">
      <h2 class="page-title">申请成为服务提供者</h2>
      
      <form class="provider-apply-form" @submit.prevent="handleSubmit">
        <div class="form-section">
          <h3>个人信息</h3>
          
          <div class="form-group">
            <label for="name" class="form-label">姓名</label>
            <input 
              type="text" 
              id="name" 
              v-model="applyForm.name" 
              class="form-input" 
              placeholder="请输入姓名"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="phone" class="form-label">联系电话</label>
            <input 
              type="tel" 
              id="phone" 
              v-model="applyForm.phone" 
              class="form-input" 
              placeholder="请输入联系电话"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="email" class="form-label">邮箱</label>
            <input 
              type="email" 
              id="email" 
              v-model="applyForm.email" 
              class="form-input" 
              placeholder="请输入邮箱"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="avatar" class="form-label">个人照片</label>
            <input 
              type="file" 
              id="avatar" 
              class="form-input" 
              accept="image/*"
              @change="handleFileUpload"
              required
            />
            <div v-if="applyForm.avatar" class="avatar-preview">
              <img :src="applyForm.avatar" alt="个人照片预览" />
            </div>
          </div>
        </div>
        
        <div class="form-section">
          <h3>服务信息</h3>
          
          <div class="form-group">
            <label for="services" class="form-label">提供的服务</label>
            <div class="service-checkboxes">
              <label v-for="service in serviceTypes" :key="service.id" class="checkbox-label">
                <input 
                  type="checkbox" 
                  :value="service.id" 
                  v-model="applyForm.services"
                />
                {{ service.name }}
              </label>
            </div>
          </div>
          
          <div class="form-group">
            <label for="experience" class="form-label">相关经验</label>
            <textarea 
              id="experience" 
              v-model="applyForm.experience" 
              class="form-input" 
              placeholder="请描述您的宠物相关经验"
              rows="4"
              required
            ></textarea>
          </div>
          
          <div class="form-group">
            <label for="certificate" class="form-label">相关证书（可选）</label>
            <input 
              type="file" 
              id="certificate" 
              class="form-input" 
              accept="image/*"
              @change="handleCertificateUpload"
            />
            <div v-if="applyForm.certificate" class="certificate-preview">
              <img :src="applyForm.certificate" alt="证书预览" />
            </div>
          </div>
        </div>
        
        <div class="form-section">
          <h3>其他信息</h3>
          
          <div class="form-group">
            <label for="address" class="form-label">服务区域</label>
            <input 
              type="text" 
              id="address" 
              v-model="applyForm.address" 
              class="form-input" 
              placeholder="请输入您能提供服务的区域"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="availability" class="form-label">可用时间</label>
            <textarea 
              id="availability" 
              v-model="applyForm.availability" 
              class="form-input" 
              placeholder="请描述您的可用时间，例如：周一至周五 9:00-18:00"
              rows="4"
              required
            ></textarea>
          </div>
        </div>
        
        <div class="form-actions">
          <button type="submit" class="btn-primary" :disabled="isLoading">
            <span v-if="isLoading" class="loading"></span>
            <span v-else>提交申请</span>
          </button>
          <router-link to="/" class="btn-outline">取消</router-link>
        </div>
      </form>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useProviderStore } from '../../store/provider'

const router = useRouter()
const providerStore = useProviderStore()

// 服务类型列表
const serviceTypes = ref([
  { id: 1, name: '宠物陪伴' },
  { id: 2, name: '宠物喂食' },
  { id: 3, name: '宠物遛弯' },
  { id: 4, name: '宠物清洁' }
])

// 申请表单
const applyForm = ref({
  name: '',
  phone: '',
  email: '',
  avatar: '',
  services: [],
  experience: '',
  certificate: '',
  address: '',
  availability: ''
})

// 加载状态
const isLoading = ref(false)

// 处理文件上传
const handleFileUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    // 这里应该上传文件到服务器，暂时使用模拟的图片URL
    applyForm.value.avatar = 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=person%20avatar%2C%20simple%20style&image_size=square'
  }
}

// 处理证书上传
const handleCertificateUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    // 这里应该上传文件到服务器，暂时使用模拟的图片URL
    applyForm.value.certificate = 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=certificate%20document%2C%20simple%20style&image_size=square'
  }
}

// 处理表单提交
const handleSubmit = async () => {
  isLoading.value = true
  try {
    await providerStore.applyProvider(applyForm.value)
    // 提交成功后跳转到成功页面
    router.push('/')
  } catch (error) {
    console.error('提交申请失败', error)
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.provider-apply {
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

.provider-apply-form {
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

.form-group {
  margin-bottom: var(--spacing-md);
  position: relative;
}

.form-label {
  display: block;
  margin-bottom: var(--spacing-xs);
  font-weight: 500;
  color: var(--text-color);
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

.service-checkboxes {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-md);
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  cursor: pointer;
  color: var(--text-color);
}

.avatar-preview,
.certificate-preview {
  margin-top: var(--spacing-sm);
  width: 100px;
  height: 100px;
  border-radius: var(--border-radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.avatar-preview img,
.certificate-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
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
  
  .provider-apply-form {
    padding: var(--spacing-lg);
    margin: 0 var(--spacing-md) var(--spacing-xl);
  }
  
  .service-checkboxes {
    flex-direction: column;
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

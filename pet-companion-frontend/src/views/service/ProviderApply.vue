<template>
  <div class="provider-apply">
    <Header />
    
    <div class="container">
      <h2 class="page-title">申请成为服务提供者</h2>

      <form class="provider-apply-form" @submit.prevent="handleSubmit">
        <div class="form-section">
          <h3>身份信息</h3>

          <div class="form-group">
            <label for="realName" class="form-label">真实姓名</label>
            <input
                type="text"
                id="realName"
                v-model="applyForm.realName"
                class="form-input"
                placeholder="请输入您的真实姓名"
                required
            />
          </div>

          <div class="form-group">
            <label for="idCard" class="form-label">身份证号</label>
            <input
                type="text"
                id="idCard"
                v-model="applyForm.idCard"
                class="form-input"
                placeholder="请输入18位身份证号"
                required
            />
          </div>
        </div>

        <div class="form-section">
          <h3>服务经验</h3>

          <div class="form-group">
            <label for="experience" class="form-label">相关经验描述</label>
            <textarea
                id="experience"
                v-model="applyForm.experience"
                class="form-input"
                placeholder="请描述您照顾宠物的经验（例如：养猫5年，擅长处理猫咪应激反应...）"
                rows="6"
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

<script setup>import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useProviderStore } from '../../store/provider'

const router = useRouter()
const providerStore = useProviderStore()



// ✅ 修改：申请表单字段与后端 DTO 完全对齐
const applyForm = ref({
  realName: '',
  idCard: '',
  experience: ''
})

// 加载状态
const isLoading = ref(false)



// 处理表单提交
const handleSubmit = async () => {
  if (!applyForm.value.realName || !applyForm.value.idCard || !applyForm.value.experience) {
    alert('请填写完整所有必填项')
    return
  }

  isLoading.value = true
  try {
    await providerStore.applyProvider(applyForm.value)
    // 提交成功后跳转到首页或提示页
    router.push('/')
  } catch (error) {
    console.error('提交申请失败', error)
    alert(error.response?.data?.msg || '申请提交失败，请稍后重试')
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

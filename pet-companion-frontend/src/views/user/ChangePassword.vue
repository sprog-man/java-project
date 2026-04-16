<template>
  <div class="change-password">
    <Header />

    <div class="container">
      <h2 class="page-title">修改密码</h2>

      <div class="password-form-card">
        <form @submit.prevent="handleSubmit">
          <!-- 原密码 -->
          <div class="form-group">
            <label for="oldPassword" class="form-label">原密码</label>
            <input
                type="password"
                id="oldPassword"
                v-model="passwordForm.oldPassword"
                class="form-input"
                placeholder="请输入原密码"
                required
            />
          </div>

          <!-- 新密码 -->
          <div class="form-group">
            <label for="newPassword" class="form-label">新密码</label>
            <input
                type="password"
                id="newPassword"
                v-model="passwordForm.newPassword"
                class="form-input"
                placeholder="请输入新密码（至少6位）"
                required
                minlength="6"
            />
            <p class="form-hint">密码长度至少为6个字符</p>
          </div>

          <!-- 确认新密码 -->
          <div class="form-group">
            <label for="confirmPassword" class="form-label">确认新密码</label>
            <input
                type="password"
                id="confirmPassword"
                v-model="passwordForm.confirmPassword"
                class="form-input"
                placeholder="请再次输入新密码"
                required
            />
            <p v-if="passwordMismatch" class="form-error">两次输入的密码不一致</p>
          </div>

          <!-- 操作按钮 -->
          <div class="form-actions">
            <button type="button" class="btn-outline" @click="goBack">取消</button>
            <button
                type="submit"
                class="btn-primary"
                :disabled="isLoading || passwordMismatch"
            >
              <span v-if="isLoading" class="loading"></span>
              <span v-else>确认修改</span>
            </button>
          </div>
        </form>
      </div>
    </div>

    <Footer />
  </div>
</template>
<script setup>import {ref, computed} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useUserStore } from '../../store/user'

const router = useRouter()
const userStore = useUserStore()


//密码表单数据
const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

//加载状态
const isLoading = ref(false)

// 计算属性：检查两次密码是否一致
const passwordMismatch = computed(() => {
  return passwordForm.value.newPassword &&
      passwordForm.value.confirmPassword &&
      passwordForm.value.newPassword !== passwordForm.value.confirmPassword
})

//返回上一页
const goBack = () => {
  router.push('/user/center')
}

//表单验证
const validateForm = () => {
  //检查原密码
  if (!passwordForm.value.oldPassword) {
    ElMessage.error('请输入原密码')
    return false
  }
  //检查新密码长度
  if (passwordForm.value.newPassword.length < 6) {
    ElMessage.error('新密码长度至少为6个字符')
    return false
  }

  // ✅ 修正：直接判断计算属性的布尔值
  if (passwordMismatch.value) {
    ElMessage.error('两次输入的密码不一致')
    return false
  }

  // 检查新旧密码是否相同
  if (passwordForm.value.newPassword === passwordForm.value.oldPassword) {
    ElMessage.warning('新密码不能与原密码相同')
    return false
  }
  return true
}

//处理表单提交
const handleSubmit = async () => {
  // 修正验证逻辑：直接判断 passwordMismatch.value
  if (!validateForm()) {
    return
  }

  isLoading.value = true

  try {
    console.log('开始修改密码...')

    // 构造符合后端 ChangePasswordRequest DTO 的 JSON 数据
    const submitData = {
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword
    }

    console.log('提交数据:', submitData)

    // 调用 Store 中的方法发送请求
    await userStore.changePassword(submitData)

    ElMessage.success('密码修改成功，请重新登录')

    // 清空表单
    passwordForm.value = {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    }

    // 延迟跳转并退出登录
    setTimeout(() => {
      userStore.logout()
      router.push('/login')
    }, 1500)

  } catch (error) {
    console.error('修改密码失败:', error)

    // 根据错误类型给出提示
    if (error.message && error.message.includes('原密码')) {
      ElMessage.error('原密码错误')
    } else {
      ElMessage.error(error.message || '修改密码失败，请检查网络连接')
    }
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>.change-password {
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

.password-form-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
  max-width: 500px;
  margin: 0 auto;
  margin-bottom: var(--spacing-2xl);
}

.form-group {
  margin-bottom: var(--spacing-lg);
}

.form-label {
  display: block;
  margin-bottom: var(--spacing-sm);
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

.form-hint {
  margin-top: var(--spacing-xs);
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.form-error {
  margin-top: var(--spacing-xs);
  font-size: 0.875rem;
  color: #dc2626;
}

.form-actions {
  display: flex;
  gap: var(--spacing-md);
  justify-content: flex-end;
  margin-top: var(--spacing-xl);
}

.btn-outline {
  padding: var(--spacing-sm) var(--spacing-lg);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-md);
  background-color: white;
  color: var(--text-color);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.btn-outline:hover {
  border-color: var(--cta-color);
  color: var(--cta-color);
}

.btn-primary {
  padding: var(--spacing-sm) var(--spacing-lg);
  border: none;
  border-radius: var(--border-radius-md);
  background-color: var(--cta-color);
  color: white;
  cursor: pointer;
  transition: background-color var(--transition-fast);
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.btn-primary:hover:not(:disabled) {
  background-color: var(--primary-color);
}

.btn-primary:disabled {
  background-color: var(--border-color);
  cursor: not-allowed;
}

.loading {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
  }

  .password-form-card {
    padding: var(--spacing-lg);
    margin: 0 var(--spacing-md);
  }

  .form-actions {
    flex-direction: column;
  }

  .btn-outline,
  .btn-primary {
    width: 100%;
    justify-content: center;
  }
}
</style>
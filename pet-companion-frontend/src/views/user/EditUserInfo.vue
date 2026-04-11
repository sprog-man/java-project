<template>
  <div class="edit-user-info">
    <Header />

    <div class="container">
      <h2 class="page-title">编辑个人资料</h2>

      <div class="edit-form-card">
        <form @submit.prevent="handleSubmit">
          <!-- 昵称 -->
          <div class="form-group">
            <label for="nickname" class="form-label">昵称</label>
            <input
                type="text"
                id="nickname"
                v-model="editForm.nickname"
                class="form-input"
                placeholder="请输入昵称"
            />
          </div>

          <!-- 邮箱 -->
          <div class="form-group">
            <label for="email" class="form-label">邮箱</label>
            <input
                type="email"
                id="email"
                v-model="editForm.email"
                class="form-input"
                placeholder="请输入邮箱"
            />
          </div>

          <div class="form-actions">
            <button type="button" class="btn-outline" @click="goBack">取消</button>
            <button type="submit" class="btn-primary" :disabled="isLoading">
              <span v-if="isLoading" class="loading"></span>
              <span v-else>保存修改</span>
            </button>
          </div>
        </form>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script setup>import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useUserStore } from '../../store/user'
import axios from '../../api'

const router = useRouter()
const userStore = useUserStore()

// 编辑表单：只包含允许修改的字段
const editForm = ref({
  nickname: '',
  email: ''
})

// 加载状态
const isLoading = ref(false)

// 回退
const goBack = () => {
  router.push('/user/center')
}

// 处理表单提交
const handleSubmit = async () => {
  isLoading.value = true
  try {
    // 构造符合后端 UpdateProfileRequest DTO 的数据
    const submitData = {
      nickname: editForm.value.nickname,
      email: editForm.value.email
    }

    // 直接调用 Store 的方法
    await userStore.updateUserInfo(submitData)

    ElMessage.success('资料修改成功')

    // 因为 Store 里可能已经更新了 user 状态，或者你可以再次调用 getUserInfo 确保最新
    await userStore.getUserInfo()

    setTimeout(() => {
      router.push('/user/center')
    }, 1000)
  } catch (error) {
    ElMessage.error(error.message || '修改失败')
  } finally {
    isLoading.value = false
  }
}

// 初始化表单数据
onMounted(async () => {
  try {
    // 获取用户信息
    await userStore.getUserInfo()

    // 填充表单数据
    const user = userStore.getUser
    if (user) {
      editForm.value.nickname = user.nickname || ''
      editForm.value.email = user.email || ''
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败，请检查网络连接')
  }
})
</script>

<style scoped>
.edit-user-info {
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

.edit-form-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
  max-width: 500px; /* 调小宽度，因为表单项变少了 */
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

.btn-primary:hover {
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

  .edit-form-card {
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

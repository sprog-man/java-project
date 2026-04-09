<template>
  <div class="edit-user-info">
    <Header />

    <div class="container">
      <h2 class="page-title">编辑个人资料</h2>

      <div class="edit-form-card">
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="username" class="form-label">用户名</label>
            <input
                type="text"
                id="username"
                v-model="editForm.username"
                class="form-input"
                placeholder="请输入用户名"
                required
            />
          </div>

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

          <div class="form-group">
            <label for="phone" class="form-label">手机号</label>
            <input
                type="tel"
                id="phone"
                v-model="editForm.phone"
                class="form-input"
                placeholder="请输入手机号"
                required
            />
          </div>

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

// 编辑表单
const editForm = ref({
  id: '',
  username: '',
  nickname: '',
  phone: '',
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
    // 确保 id 存在
    if (!editForm.value.id) {
      ElMessage.error('用户信息不完整，请重新登录')
      return
    }

    // 确保 id 是数字类型
    const submitData = {
      ...editForm.value,
      id: parseInt(editForm.value.id)
    }

    console.log('提交的数据:', submitData)

    // 发送修改请求
    const response = await axios.put('/user/center/modify', submitData)
    console.log('修改成功:', response)

    if (response.data && response.data.code === 200) {
      ElMessage.success('修改成功，为了账号安全，请重新登录')
    // 退出登录
    userStore.logout()

    // 跳转到登录页面
      setTimeout(() => {
        router.push('/login')
      }, 1500)
    } else {
      ElMessage.error(response.data?.message || '修改失败，请稍后重试')
    }
  } catch (error) {
    console.error('修改失败:', error)

    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message

      switch (status) {
        case 401:
          ElMessage.error('未授权，请重新登录')
          break
        case 403:
          ElMessage.error('没有权限执行此操作')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器错误，请稍后重试')
          break
        default:
          ElMessage.error(message || '修改失败，请检查网络连接')
      }
    } else if (error.request) {
      ElMessage.error('网络错误，请检查网络连接')
    } else {
      ElMessage.error('请求配置错误')
    }
  } finally {
    isLoading.value = false
  }
}

// 初始化表单数据
onMounted(async () => {
  try {
    console.log('开始获取用户信息...')
    // 获取用户信息
    await userStore.getUserInfo()
    console.log('获取用户信息成功')

    // 填充表单数据
    const user = userStore.getUser
    console.log('用户信息:', user)
    
    if (user && user.id) {
      editForm.value.id = user.id
      editForm.value.username = user.username || ''
      editForm.value.nickname = user.nickname || ''
      editForm.value.phone = user.phone || ''
      editForm.value.email = user.email || ''
      console.log('表单数据填充完成')
    } else {
      console.error('用户信息不完整，缺少id')
      // 尝试从localStorage获取用户信息
      const cachedUserInfo = localStorage.getItem('userInfo')
      if (cachedUserInfo) {
        try {
          const userData = JSON.parse(cachedUserInfo)
          if (userData.id) {
            editForm.value.id = userData.id
            editForm.value.username = userData.username || ''
            editForm.value.nickname = userData.nickname || ''
            editForm.value.phone = userData.phone || ''
            editForm.value.email = userData.email || ''
            console.log('从缓存获取用户信息')
          }
        } catch (e) {
          console.error('解析缓存用户信息失败:', e)
        }
      }
      ElMessage.error('用户信息不完整，可能需要重新登录')
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    if (error.response) {
      console.error('错误响应:', error.response)
      console.error('错误状态:', error.response.status)
      console.error('错误数据:', error.response.data)
    }
    // 尝试从localStorage获取用户信息
    const cachedUserInfo = localStorage.getItem('userInfo')
    if (cachedUserInfo) {
      try {
        const userData = JSON.parse(cachedUserInfo)
        if (userData.id) {
          editForm.value.id = userData.id
          editForm.value.username = userData.username || ''
          editForm.value.nickname = userData.nickname || ''
          editForm.value.phone = userData.phone || ''
          editForm.value.email = userData.email || ''
          console.log('从缓存获取用户信息')
        }
      } catch (e) {
        console.error('解析缓存用户信息失败:', e)
      }
    }
    ElMessage.error('获取用户信息失败，可能需要重新登录')
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
  max-width: 600px;
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
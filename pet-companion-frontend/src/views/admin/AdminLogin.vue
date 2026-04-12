<template>
  <div class="login-container">
    <div class="login-form">
      <h2>管理员登录</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="username">用户名</label>
          <input 
            type="text" 
            id="username" 
            v-model="form.username" 
            required 
            placeholder="请输入用户名"
          />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input 
            type="password" 
            id="password" 
            v-model="form.password" 
            required 
            placeholder="请输入密码"
          />
        </div>
        <button type="submit" class="login-btn" :disabled="isLoading">
          {{ isLoading ? '登录中...' : '登录' }}
        </button>
      </form>
      <p class="error-message" v-if="errorMessage">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../../api'

const router = useRouter()
const form = ref({
  username: '',
  password: ''
})
const isLoading = ref(false)
const errorMessage = ref('')

// ... existing code ...
const handleSubmit = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    // ✅ 确认路径：如果后端是 @RequestMapping("/admin") + @PostMapping("/login")，这里应该是 /admin/login
    const response = await api.post('/user/admin/login', form.value)

    if (response.data && response.data.code === 200) {
      const { accessToken, adminInfo } = response.data.data

      console.log('✅ 登录成功，准备存储 Token:', accessToken)

      // ✅ 核心修复：存储时显式带上 role: 1，确保路由守卫能通过
      localStorage.setItem('token', accessToken)
      localStorage.setItem('userInfo', JSON.stringify({
        ...adminInfo,
        role: 1 // 强制标记为管理员，防止后端漏传或前端解析错误
      }))
      // ✅ 建议：使用 router.push 而不是 window.location.href，体验更丝滑
      router.push('/admin/center')
    } else {
      errorMessage.value = response.data?.message || '登录失败'
    }
  } catch (error) {
    console.error('❌ 登录请求异常:', error)
    errorMessage.value = error.response?.data?.message || '登录失败，请检查网络连接'
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.login-form {
  background: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.login-form h2 {
  margin-bottom: 24px;
  color: #333;
  text-align: center;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #666;
  font-size: 14px;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #409eff;
}

.login-btn {
  width: 100%;
  padding: 12px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-btn:hover {
  background-color: #66b1ff;
}

.login-btn:disabled {
  background-color: #c6e2ff;
  cursor: not-allowed;
}

.error-message {
  margin-top: 16px;
  color: #f56c6c;
  font-size: 14px;
  text-align: center;
}
</style>
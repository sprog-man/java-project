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
    const response = await api.post('/user/admin/login', form.value)

    // 修正：适配后端 Result 类的返回格式 (code: 200)
    if (response.data && response.data.code === 200) {
      const { accessToken, adminInfo } = response.data.data

      console.log('管理员登录成功:', adminInfo)

      // 存储 Token 和管理员信息
      localStorage.setItem('token', accessToken)
      localStorage.setItem('userInfo', JSON.stringify({
        id: adminInfo.id,
        username: adminInfo.username,
        nickname: adminInfo.nickname,
        avatar: adminInfo.avatar,
        role: 1 // 强制设置为管理员角色
      }))

      // 跳转到后台中心
      router.push('/admin/center')
    } else {
      // 业务逻辑失败（如密码错误）
      errorMessage.value = response.data?.message || '登录失败'
    }
  } catch (error) {
    console.error('登录失败:', error)
    // 网络错误或服务器异常
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
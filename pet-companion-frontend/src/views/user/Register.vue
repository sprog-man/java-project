<template>
  <div class="register">
    <div class="container">
      <div class="register-card">
        <div class="register-header">
          <h2>用户注册</h2>
          <p>欢迎加入宠伴服务平台</p>
        </div>
        
        <form class="register-form" @submit.prevent="handleRegister">
          <div class="form-group">
            <label for="username" class="form-label">用户名</label>
            <input 
              type="text" 
              id="username" 
              v-model="registerForm.username" 
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
              v-model="registerForm.nickname" 
              class="form-input" 
              placeholder="请输入昵称（可选）"
            />
          </div>
          
          <div class="form-group">
            <label for="phone" class="form-label">手机号</label>
            <input 
              type="tel" 
              id="phone" 
              v-model="registerForm.phone" 
              class="form-input" 
              placeholder="请输入手机号"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="password" class="form-label">密码</label>
            <input 
              type="password" 
              id="password" 
              v-model="registerForm.password" 
              class="form-input" 
              placeholder="请输入密码（至少6位）"
              required
              minlength="6"
            />
          </div>
          
          <div class="form-group">
            <label for="confirmPassword" class="form-label">确认密码</label>
            <input 
              type="password" 
              id="confirmPassword" 
              v-model="registerForm.confirmPassword" 
              class="form-input" 
              placeholder="请确认密码"
              required
            />
          </div>
          
          <div class="form-group agree-group">
            <input 
              type="checkbox" 
              id="agree" 
              v-model="registerForm.agree"
              required
            />
            <label for="agree">我已阅读并同意<a href="#">服务条款</a>和<a href="#">隐私政策</a></label>
          </div>
          
          <button type="submit" class="btn-primary register-button" :disabled="isLoading">
            <span v-if="isLoading" class="loading"></span>
            <span v-else>注册</span>
          </button>
          
          <div class="register-footer">
            <p>已有账号？<router-link to="/login">立即登录</router-link></p>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store/user'

const router = useRouter()
const userStore = useUserStore()

// 注册表单
const registerForm = ref({
  username: '',
  nickname: '',
  phone: '',
  password: '',
  confirmPassword: '',
  agree: false
})

// 加载状态
const isLoading = ref(false)

// 处理注册
const handleRegister = async () => {
  // 验证密码是否一致
  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    // 可以添加错误提示
    console.error('密码不一致')
    return
  }
  
  isLoading.value = true
  try {
    await userStore.register(registerForm.value)
    // 注册成功后跳转到登录页
    router.push('/login')
  } catch (error) {
    console.error('注册失败', error)
    // 可以在这里添加错误提示
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.register {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--background-color);
  padding: var(--spacing-xl) 0;
}

.register-card {
  background-color: white;
  border-radius: var(--border-radius-xl);
  box-shadow: var(--shadow-lg);
  padding: var(--spacing-2xl);
  width: 100%;
  max-width: 400px;
}

.register-header {
  text-align: center;
  margin-bottom: var(--spacing-xl);
}

.register-header h2 {
  font-size: 1.75rem;
  margin-bottom: var(--spacing-sm);
  color: var(--primary-color);
}

.register-header p {
  color: var(--light-text-color);
}

.form-group {
  margin-bottom: var(--spacing-md);
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



.agree-group {
  display: flex;
  align-items: flex-start;
  gap: var(--spacing-xs);
}

.agree-group label {
  font-size: 0.875rem;
  color: var(--light-text-color);
  cursor: pointer;
  flex: 1;
}

.agree-group a {
  color: var(--cta-color);
  text-decoration: none;
  transition: color var(--transition-fast);
}

.agree-group a:hover {
  color: var(--primary-color);
}

.register-button {
  width: 100%;
  padding: var(--spacing-sm) 0;
  font-size: 1rem;
  font-weight: 500;
  margin-top: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-sm);
}

.register-footer {
  text-align: center;
  font-size: 0.875rem;
  color: var(--light-text-color);
}

.register-footer a {
  color: var(--cta-color);
  text-decoration: none;
  transition: color var(--transition-fast);
}

.register-footer a:hover {
  color: var(--primary-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .register-card {
    padding: var(--spacing-xl);
    margin: 0 var(--spacing-md);
  }
  
  .register-header h2 {
    font-size: 1.5rem;
  }
  

}
</style>

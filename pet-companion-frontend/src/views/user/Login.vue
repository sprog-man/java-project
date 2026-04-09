<template>
  <div class="login">
    <div class="container">
      <!-- 登录卡片 -->
      <div class="login-card">
        <!-- 登录头部 -->
        <div class="login-header">
          <h2>用户登录</h2>
          <p>欢迎回到宠伴服务平台</p>
        </div>
        
        <!-- 登录表单 -->
        <form class="login-form" @submit.prevent="handleLogin">
          <!-- 用户名/手机号/昵称输入 -->
          <div class="form-group">
            <label for="username" class="form-label">用户名/手机号/昵称</label>
            <input 
              type="text" 
              id="username" 
              v-model="loginForm.username" 
              class="form-input" 
              placeholder="请输入用户名、手机号或昵称"
              required
            />
          </div>
          
          <!-- 密码输入 -->
          <div class="form-group">
            <label for="password" class="form-label">密码</label>
            <input 
              type="password" 
              id="password" 
              v-model="loginForm.password" 
              class="form-input" 
              placeholder="请输入密码"
              required
            />
          </div>
          
          <!-- 记住密码 -->
          <div class="form-group remember-group">
            <input 
              type="checkbox" 
              id="remember" 
              v-model="loginForm.remember"
            />
            <label for="remember">记住密码</label>
          </div>
          
          <!-- 登录按钮 -->
          <button type="submit" class="btn-primary login-button" :disabled="isLoading">
            <span v-if="isLoading" class="loading"></span>
            <span v-else>登录</span>
          </button>
          
          <!-- 登录底部，包含注册链接 -->
          <div class="login-footer">
            <p>还没有账号？<router-link to="/register">立即注册</router-link></p>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
// 导入Vue的ref函数，用于创建响应式数据
import { ref } from 'vue'
// 导入Vue Router的useRouter函数，用于路由跳转
import { useRouter } from 'vue-router'
// 导入用户状态管理仓库
import { useUserStore } from '../../store/user'

// 初始化路由实例
const router = useRouter()
// 初始化用户状态管理仓库
const userStore = useUserStore()

// 登录表单数据
const loginForm = ref({
  username: '', // 用户名/手机号/昵称
  password: '', // 密码
  remember: false // 是否记住密码
})

// 加载状态，用于显示加载动画
const isLoading = ref(false)

// 处理登录
const handleLogin = async () => {
  // 设置加载状态为true
  isLoading.value = true
  try {
    // 调用userStore的login方法进行登录
    await userStore.login(loginForm.value)
    // 登录成功后跳转到首页
    router.push('/')
  } catch (error) {
    // 处理登录失败
    console.error('登录失败', error)
    // 可以在这里添加错误提示
  } finally {
    // 无论成功还是失败，都设置加载状态为false
    isLoading.value = false
  }
}
</script>

<style scoped>
/* 登录页面容器 */
.login {
  min-height: 100vh; /* 占满整个视口高度 */
  display: flex; /* 使用flex布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  background-color: var(--background-color); /* 背景颜色 */
  padding: var(--spacing-xl) 0; /* 上下内边距 */
}

/* 登录卡片 */
.login-card {
  background-color: white; /* 白色背景 */
  border-radius: var(--border-radius-xl); /* 圆角 */
  box-shadow: var(--shadow-lg); /* 阴影 */
  padding: var(--spacing-2xl); /* 内边距 */
  width: 100%; /* 宽度100% */
  max-width: 400px; /* 最大宽度400px */
}

/* 登录头部 */
.login-header {
  text-align: center; /* 文本居中 */
  margin-bottom: var(--spacing-xl); /* 底部外边距 */
}

.login-header h2 {
  font-size: 1.75rem; /* 字体大小 */
  margin-bottom: var(--spacing-sm); /* 底部外边距 */
  color: var(--primary-color); /* 文本颜色 */
}

.login-header p {
  color: var(--light-text-color); /* 文本颜色 */
}

/* 表单组 */
.form-group {
  margin-bottom: var(--spacing-md); /* 底部外边距 */
}

/* 表单标签 */
.form-label {
  display: block; /* 块级元素 */
  margin-bottom: var(--spacing-xs); /* 底部外边距 */
  font-weight: 500; /* 字体粗细 */
  color: var(--text-color); /* 文本颜色 */
}

/* 表单输入框 */
.form-input {
  width: 100%; /* 宽度100% */
  padding: var(--spacing-sm) var(--spacing-md); /* 内边距 */
  border: 1px solid var(--border-color); /* 边框 */
  border-radius: var(--border-radius-md); /* 圆角 */
  font-size: 1rem; /* 字体大小 */
  transition: border-color var(--transition-fast); /* 过渡效果 */
}

/* 输入框聚焦效果 */
.form-input:focus {
  outline: none; /* 去除默认轮廓 */
  border-color: var(--cta-color); /* 边框颜色 */
  box-shadow: 0 0 0 3px rgba(3, 105, 161, 0.1); /* 阴影效果 */
}

/* 记住密码组 */
.remember-group {
  display: flex; /* 使用flex布局 */
  align-items: center; /* 垂直居中 */
  gap: var(--spacing-xs); /* 间距 */
}

.remember-group label {
  font-size: 0.875rem; /* 字体大小 */
  color: var(--light-text-color); /* 文本颜色 */
  cursor: pointer; /* 光标样式 */
}

/* 登录按钮 */
.login-button {
  width: 100%; /* 宽度100% */
  padding: var(--spacing-sm) 0; /* 内边距 */
  font-size: 1rem; /* 字体大小 */
  font-weight: 500; /* 字体粗细 */
  margin-top: var(--spacing-md); /* 顶部外边距 */
  margin-bottom: var(--spacing-lg); /* 底部外边距 */
  display: flex; /* 使用flex布局 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  gap: var(--spacing-sm); /* 间距 */
}

/* 登录底部 */
.login-footer {
  text-align: center; /* 文本居中 */
  font-size: 0.875rem; /* 字体大小 */
  color: var(--light-text-color); /* 文本颜色 */
}

/* 注册链接 */
.login-footer a {
  color: var(--cta-color); /* 文本颜色 */
  text-decoration: none; /* 去除下划线 */
  transition: color var(--transition-fast); /* 过渡效果 */
}

/* 注册链接 hover效果 */
.login-footer a:hover {
  color: var(--primary-color); /* 文本颜色 */
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-card {
    padding: var(--spacing-xl); /* 内边距 */
    margin: 0 var(--spacing-md); /* 左右外边距 */
  }
  
  .login-header h2 {
    font-size: 1.5rem; /* 字体大小 */
  }
}
</style>

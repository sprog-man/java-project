<template>
  <header class="header">
    <div class="container">
      <div class="header-content">
        <!-- Logo -->
        <div class="logo">
          <router-link to="/">
            <h1>宠伴<span>服务</span></h1>
          </router-link>
        </div>
        
        <!-- 导航菜单 -->
        <nav class="nav">
          <ul class="nav-list">
            <li class="nav-item">
              <router-link to="/" class="nav-link">首页</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/service/type" class="nav-link">服务类型</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/order/create" class="nav-link">预约服务</router-link>
            </li>
          </ul>
        </nav>
        
        <!-- 用户菜单 -->
        <div class="user-menu">
          <template v-if="isLoggedIn">
            <!-- 已登录状态 -->
            <div class="user-dropdown">
              <button class="user-button">
                <span class="user-name">{{ user?.nickname || '用户' }}</span>
                <span class="dropdown-arrow">▼</span>
              </button>
              <div class="dropdown-menu">
                <router-link to="/user/center" class="dropdown-item">个人中心</router-link>
                <router-link to="/user/pets" class="dropdown-item">我的宠物</router-link>
                <router-link to="/user/orders" class="dropdown-item">我的订单</router-link>
                <template v-if="userType === 'USER'">
                  <router-link to="/provider/apply" class="dropdown-item">申请成为服务提供者</router-link>
                </template>
                <template v-if="userType === 'PROVIDER'">
                  <router-link to="/provider/center" class="dropdown-item">服务提供者中心</router-link>
                  <router-link to="/provider/orders" class="dropdown-item">服务订单</router-link>
                </template>
                <div class="dropdown-divider"></div>
                <button class="dropdown-item logout-button" @click="logout">退出登录</button>
              </div>
            </div>
          </template>
          <template v-else>
            <!-- 未登录状态 -->
            <router-link to="/login" class="login-button">登录</router-link>
            <router-link to="/register" class="register-button">注册</router-link>
          </template>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store/user'

const router = useRouter()
const userStore = useUserStore()

const isLoggedIn = computed(() => userStore.getIsLoggedIn)
const user = computed(() => userStore.getUser)
const userType = computed(() => userStore.getUserType)

// 退出登录
const logout = () => {
  userStore.logout()
  router.push('/login')
}

// 获取用户信息
onMounted(async () => {
  if (isLoggedIn.value) {
    try {
      await userStore.getUserInfo()
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
  }
})
</script>

<style scoped>
.header {
  position: sticky;
  top: 0;
  z-index: 1000;
  background-color: white;
  box-shadow: var(--shadow-sm);
  padding: var(--spacing-md) 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo h1 {
  font-size: 1.75rem;
  font-weight: 700;
  margin: 0;
  color: var(--primary-color);
}

.logo h1 span {
  color: var(--cta-color);
}

.nav-list {
  display: flex;
  list-style: none;
  gap: var(--spacing-xl);
}

.nav-link {
  color: var(--text-color);
  text-decoration: none;
  font-weight: 500;
  transition: color var(--transition-fast);
  padding: var(--spacing-sm) 0;
  position: relative;
}

.nav-link:hover {
  color: var(--cta-color);
}

.nav-link.router-link-active {
  color: var(--cta-color);
}

.nav-link.router-link-active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: var(--cta-color);
}

.user-menu {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.login-button {
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--border-radius-md);
  font-weight: 500;
  transition: all var(--transition-fast);
  color: var(--text-color);
}

.login-button:hover {
  color: var(--cta-color);
}

.register-button {
  padding: var(--spacing-sm) var(--spacing-md);
  background-color: var(--cta-color);
  color: white;
  border-radius: var(--border-radius-md);
  font-weight: 500;
  transition: all var(--transition-fast);
}

.register-button:hover {
  background-color: var(--primary-color);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
  color: white;
}

.user-dropdown {
  position: relative;
}

.user-button {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  padding: var(--spacing-sm) var(--spacing-md);
  background-color: transparent;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.user-button:hover {
  border-color: var(--cta-color);
  box-shadow: var(--shadow-sm);
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: var(--spacing-xs);
  background-color: white;
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-md);
  padding: var(--spacing-sm) 0;
  min-width: 180px;
  z-index: 1000;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-10px);
  transition: all var(--transition-fast);
}

.user-dropdown:hover .dropdown-menu {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.dropdown-item {
  display: block;
  padding: var(--spacing-sm) var(--spacing-md);
  color: var(--text-color);
  text-decoration: none;
  transition: all var(--transition-fast);
  cursor: pointer;
}

.dropdown-item:hover {
  background-color: var(--background-color);
  color: var(--cta-color);
}

.dropdown-divider {
  height: 1px;
  background-color: var(--border-color);
  margin: var(--spacing-xs) 0;
}

.logout-button {
  width: 100%;
  text-align: left;
  background: none;
  border: none;
  font-family: var(--body-font);
  font-size: 1rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav {
    display: none;
  }
  
  .logo h1 {
    font-size: 1.5rem;
  }
  
  .user-button {
    padding: var(--spacing-xs) var(--spacing-sm);
    font-size: 0.875rem;
  }
  
  .login-button,
  .register-button {
    padding: var(--spacing-xs) var(--spacing-sm);
    font-size: 0.875rem;
  }
}
</style>

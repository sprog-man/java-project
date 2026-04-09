<template>
  <div class="admin-container">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>宠物陪伴平台</h2>
        <p>管理员后台</p>
      </div>
      <nav class="sidebar-nav">
        <ul>
          <li>
            <router-link to="/admin/center" class="nav-item">
              <span class="nav-icon">🏠</span>
              <span class="nav-text">首页</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/users" class="nav-item active">
              <span class="nav-icon">👥</span>
              <span class="nav-text">用户管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/services" class="nav-item">
              <span class="nav-icon">🏪</span>
              <span class="nav-text">服务管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/orders" class="nav-item">
              <span class="nav-icon">📋</span>
              <span class="nav-text">订单管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/pets" class="nav-item">
              <span class="nav-icon">🐶</span>
              <span class="nav-text">宠物管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/admin/reviews" class="nav-item">
              <span class="nav-icon">⭐</span>
              <span class="nav-text">评价管理</span>
            </router-link>
          </li>
          <li class="logout">
            <a href="#" @click.prevent="handleLogout" class="nav-item">
              <span class="nav-icon">🚪</span>
              <span class="nav-text">退出登录</span>
            </a>
          </li>
        </ul>
      </nav>
    </aside>
    
    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 顶部导航 -->
      <header class="top-nav">
        <div class="top-nav-left">
          <h3>用户管理</h3>
        </div>
        <div class="top-nav-right">
          <span class="user-info">欢迎，{{ userInfo?.nickname || userInfo?.username }}</span>
          <button class="logout-btn" @click="handleLogout">退出登录</button>
        </div>
      </header>
      
      <!-- 内容区 -->
      <div class="content">
        <!-- 搜索和筛选 -->
        <div class="search-filter">
          <div class="search-box">
            <input 
              type="text" 
              v-model="searchQuery" 
              placeholder="搜索用户名或手机号"
              @input="handleSearch"
            />
            <button class="search-btn">搜索</button>
          </div>
          <div class="filter-box">
            <select v-model="filters.userType" @change="handleFilter">
              <option value="">全部用户类型</option>
              <option value="0">宠物主</option>
              <option value="1">服务提供者</option>
              <option value="2">两者皆是</option>
            </select>
            <select v-model="filters.status" @change="handleFilter">
              <option value="">全部状态</option>
              <option value="0">正常</option>
              <option value="1">禁用</option>
            </select>
            <select v-model="filters.verified" @change="handleFilter">
              <option value="">全部认证状态</option>
              <option value="0">未认证</option>
              <option value="1">已认证</option>
            </select>
            <select v-model="filters.role" @change="handleFilter">
              <option value="">全部角色</option>
              <option value="0">普通用户</option>
              <option value="1">管理员</option>
            </select>
          </div>
        </div>
        
        <!-- 用户列表 -->
        <div class="user-list">
          <table class="user-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>用户名</th>
                <th>手机号</th>
                <th>昵称</th>
                <th>用户类型</th>
                <th>状态</th>
                <th>认证状态</th>
                <th>角色</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in users" :key="user.id">
                <td>{{ user.id }}</td>
                <td>{{ user.username }}</td>
                <td>{{ user.phone }}</td>
                <td>{{ user.nickname }}</td>
                <td>{{ getUserTypeText(user.userType) }}</td>
                <td>{{ getUserStatusText(user.status) }}</td>
                <td>{{ getUserVerifiedText(user.verified) }}</td>
                <td>{{ getUserRoleText(user.role) }}</td>
                <td>{{ formatDate(user.createTime) }}</td>
                <td class="action-buttons">
                  <button class="edit-btn" @click="editUser(user)">编辑</button>
                  <button class="delete-btn" @click="deleteUser(user.id)">删除</button>
                </td>
              </tr>
            </tbody>
          </table>
          
          <!-- 分页 -->
          <div class="pagination">
            <button 
              class="page-btn" 
              :disabled="currentPage === 1" 
              @click="changePage(currentPage - 1)"
            >
              上一页
            </button>
            <span class="page-info">
              第 {{ currentPage }} 页，共 {{ totalPages }} 页
            </span>
            <button 
              class="page-btn" 
              :disabled="currentPage === totalPages" 
              @click="changePage(currentPage + 1)"
            >
              下一页
            </button>
          </div>
        </div>
        
        <!-- 编辑用户对话框 -->
        <div class="modal" v-if="showEditModal">
          <div class="modal-content">
            <div class="modal-header">
              <h3>编辑用户</h3>
              <button class="close-btn" @click="showEditModal = false">&times;</button>
            </div>
            <div class="modal-body">
              <form @submit.prevent="submitEdit">
                <div class="form-group">
                  <label>用户名</label>
                  <input type="text" v-model="editForm.username" disabled />
                </div>
                <div class="form-group">
                  <label>手机号</label>
                  <input type="text" v-model="editForm.phone" disabled />
                </div>
                <div class="form-group">
                  <label>昵称</label>
                  <input type="text" v-model="editForm.nickname" />
                </div>
                <div class="form-group">
                  <label>用户类型</label>
                  <select v-model="editForm.userType">
                    <option value="0">宠物主</option>
                    <option value="1">服务提供者</option>
                    <option value="2">两者皆是</option>
                  </select>
                </div>
                <div class="form-group">
                  <label>状态</label>
                  <select v-model="editForm.status">
                    <option value="0">正常</option>
                    <option value="1">禁用</option>
                  </select>
                </div>
                <div class="form-group">
                  <label>认证状态</label>
                  <select v-model="editForm.verified">
                    <option value="0">未认证</option>
                    <option value="1">已认证</option>
                  </select>
                </div>
                <div class="form-group">
                  <label>角色</label>
                  <select v-model="editForm.role">
                    <option value="0">普通用户</option>
                    <option value="1">管理员</option>
                  </select>
                </div>
                <div class="modal-footer">
                  <button type="button" class="cancel-btn" @click="showEditModal = false">取消</button>
                  <button type="submit" class="save-btn">保存</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import api from '../../api'

const router = useRouter()
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo')))
const users = ref([])
const searchQuery = ref('')
const filters = ref({
  userType: '',
  status: '',
  verified: '',
  role: ''
})
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const showEditModal = ref(false)
const editForm = ref({
  id: '',
  username: '',
  phone: '',
  nickname: '',
  userType: 0,
  status: 0,
  verified: 0,
  role: 0
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  router.push('/admin/login')
}

const getUserTypeText = (userType) => {
  const types = {
    0: '宠物主',
    1: '服务提供者',
    2: '两者皆是'
  }
  return types[userType] || '未知'
}

const getUserStatusText = (status) => {
  return status === 0 ? '正常' : '禁用'
}

const getUserVerifiedText = (verified) => {
  return verified === 0 ? '未认证' : '已认证'
}

const getUserRoleText = (role) => {
  return role === 0 ? '普通用户' : '管理员'
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

const handleSearch = () => {
  // 实现搜索逻辑
  fetchUsers()
}

const handleFilter = () => {
  // 实现筛选逻辑
  fetchUsers()
}

const changePage = (page) => {
  currentPage.value = page
  fetchUsers()
}

const editUser = (user) => {
  editForm.value = {
    id: user.id,
    username: user.username,
    phone: user.phone,
    nickname: user.nickname,
    userType: user.userType,
    status: user.status,
    verified: user.verified,
    role: user.role
  }
  showEditModal.value = true
}

const submitEdit = async () => {
  try {
    // 实现编辑用户逻辑
    console.log('编辑用户:', editForm.value)
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    showEditModal.value = false
    fetchUsers()
  } catch (error) {
    console.error('编辑用户失败:', error)
  }
}

const deleteUser = async (userId) => {
  if (!confirm('确定要删除这个用户吗？')) return
  try {
    // 实现删除用户逻辑
    console.log('删除用户:', userId)
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    fetchUsers()
  } catch (error) {
    console.error('删除用户失败:', error)
  }
}

const fetchUsers = async () => {
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    // 模拟数据
    users.value = [
      {
        id: 1,
        username: 'admin',
        phone: '13800138000',
        nickname: '管理员',
        userType: 0,
        status: 0,
        verified: 1,
        role: 1,
        createTime: '2026-01-01T00:00:00'
      },
      {
        id: 2,
        username: 'user1',
        phone: '13800138001',
        nickname: '用户1',
        userType: 0,
        status: 0,
        verified: 0,
        role: 0,
        createTime: '2026-01-02T00:00:00'
      },
      {
        id: 3,
        username: 'provider1',
        phone: '13800138002',
        nickname: '服务提供者1',
        userType: 1,
        status: 0,
        verified: 1,
        role: 0,
        createTime: '2026-01-03T00:00:00'
      },
      {
        id: 4,
        username: 'user2',
        phone: '13800138003',
        nickname: '用户2',
        userType: 0,
        status: 1,
        verified: 0,
        role: 0,
        createTime: '2026-01-04T00:00:00'
      },
      {
        id: 5,
        username: 'provider2',
        phone: '13800138004',
        nickname: '服务提供者2',
        userType: 1,
        status: 0,
        verified: 1,
        role: 0,
        createTime: '2026-01-05T00:00:00'
      }
    ]
    totalPages.value = 1
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.admin-container {
  display: flex;
  min-height: 100vh;
  background-color: #f0f2f5;
}

/* 侧边栏 */
.sidebar {
  width: 240px;
  background-color: #1f2329;
  color: white;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #30363d;
}

.sidebar-header h2 {
  margin: 0 0 5px 0;
  font-size: 18px;
}

.sidebar-header p {
  margin: 0;
  font-size: 14px;
  color: #8b949e;
}

.sidebar-nav {
  flex: 1;
  padding: 20px 0;
}

.sidebar-nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-nav li {
  margin: 0;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: #8b949e;
  text-decoration: none;
  transition: all 0.3s;
}

.nav-item:hover {
  background-color: #282f38;
  color: white;
}

.nav-item.active {
  background-color: #238636;
  color: white;
}

.nav-icon {
  margin-right: 12px;
  font-size: 18px;
}

.nav-text {
  font-size: 14px;
}

.logout {
  margin-top: auto;
  margin-bottom: 20px;
}

/* 主内容区 */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 顶部导航 */
.top-nav {
  background-color: white;
  padding: 0 30px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.top-nav-left h3 {
  margin: 0;
  font-size: 16px;
  color: #24292f;
}

.top-nav-right {
  display: flex;
  align-items: center;
}

.user-info {
  margin-right: 20px;
  font-size: 14px;
  color: #57606a;
}

.logout-btn {
  background: none;
  border: 1px solid #d0d7de;
  border-radius: 6px;
  padding: 6px 12px;
  font-size: 14px;
  color: #24292f;
  cursor: pointer;
  transition: all 0.3s;
}

.logout-btn:hover {
  background-color: #f6f8fa;
  border-color: #d0d7de;
}

/* 内容区 */
.content {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
}

/* 搜索和筛选 */
.search-filter {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.search-box {
  display: flex;
  gap: 10px;
}

.search-box input {
  padding: 8px 12px;
  border: 1px solid #d0d7de;
  border-radius: 6px;
  font-size: 14px;
  width: 300px;
}

.search-btn {
  padding: 8px 16px;
  background-color: #238636;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-btn:hover {
  background-color: #2ea043;
}

.filter-box {
  display: flex;
  gap: 10px;
  align-items: center;
}

.filter-box select {
  padding: 8px 12px;
  border: 1px solid #d0d7de;
  border-radius: 6px;
  font-size: 14px;
  min-width: 120px;
}

/* 用户列表 */
.user-list {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
}

.user-table th,
.user-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #eaecef;
}

.user-table th {
  background-color: #f6f8fa;
  font-weight: 600;
  font-size: 14px;
  color: #24292f;
}

.user-table td {
  font-size: 14px;
  color: #57606a;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.edit-btn {
  padding: 6px 12px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.edit-btn:hover {
  background-color: #66b1ff;
}

.delete-btn {
  padding: 6px 12px;
  background-color: #f56c6c;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.delete-btn:hover {
  background-color: #f78989;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  gap: 16px;
}

.page-btn {
  padding: 6px 12px;
  background-color: white;
  border: 1px solid #d0d7de;
  border-radius: 6px;
  font-size: 14px;
  color: #24292f;
  cursor: pointer;
  transition: all 0.3s;
}

.page-btn:hover:not(:disabled) {
  background-color: #f6f8fa;
  border-color: #d0d7de;
}

.page-btn:disabled {
  color: #8b949e;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: #57606a;
}

/* 模态框 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eaecef;
}

.modal-header h3 {
  margin: 0;
  font-size: 16px;
  color: #24292f;
}

.close-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: #8b949e;
  transition: color 0.3s;
}

.close-btn:hover {
  color: #24292f;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #24292f;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d0d7de;
  border-radius: 6px;
  font-size: 14px;
}

.form-group input:disabled {
  background-color: #f6f8fa;
  cursor: not-allowed;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px;
  border-top: 1px solid #eaecef;
  margin-top: 20px;
}

.cancel-btn {
  padding: 8px 16px;
  background-color: white;
  border: 1px solid #d0d7de;
  border-radius: 6px;
  font-size: 14px;
  color: #24292f;
  cursor: pointer;
  transition: all 0.3s;
}

.cancel-btn:hover {
  background-color: #f6f8fa;
  border-color: #d0d7de;
}

.save-btn {
  padding: 8px 16px;
  background-color: #238636;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.save-btn:hover {
  background-color: #2ea043;
}
</style>
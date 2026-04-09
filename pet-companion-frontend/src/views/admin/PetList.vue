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
            <router-link to="/admin/users" class="nav-item">
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
            <router-link to="/admin/pets" class="nav-item active">
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
          <h3>宠物管理</h3>
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
              placeholder="搜索宠物名称"
              @input="handleSearch"
            />
            <button class="search-btn">搜索</button>
          </div>
          <div class="filter-box">
            <select v-model="filters.type" @change="handleFilter">
              <option value="">全部类型</option>
              <option value="狗">狗</option>
              <option value="猫">猫</option>
              <option value="其他">其他</option>
            </select>
            <select v-model="filters.gender" @change="handleFilter">
              <option value="">全部性别</option>
              <option value="公">公</option>
              <option value="母">母</option>
            </select>
          </div>
        </div>
        
        <!-- 宠物列表 -->
        <div class="pet-list">
          <table class="pet-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>宠物名称</th>
                <th>类型</th>
                <th>性别</th>
                <th>年龄</th>
                <th>主人</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="pet in pets" :key="pet.id">
                <td>{{ pet.id }}</td>
                <td>{{ pet.name }}</td>
                <td>{{ pet.type }}</td>
                <td>{{ pet.gender }}</td>
                <td>{{ pet.age }}</td>
                <td>{{ pet.ownerName }}</td>
                <td>{{ formatDate(pet.createTime) }}</td>
                <td class="action-buttons">
                  <button class="detail-btn" @click="viewPetDetail(pet.id)">详情</button>
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
        
        <!-- 宠物详情对话框 -->
        <div class="modal" v-if="showDetailModal">
          <div class="modal-content">
            <div class="modal-header">
              <h3>宠物详情</h3>
              <button class="close-btn" @click="showDetailModal = false">&times;</button>
            </div>
            <div class="modal-body">
              <div class="pet-detail">
                <div class="detail-item">
                  <label>宠物名称:</label>
                  <span>{{ currentPet.name }}</span>
                </div>
                <div class="detail-item">
                  <label>类型:</label>
                  <span>{{ currentPet.type }}</span>
                </div>
                <div class="detail-item">
                  <label>品种:</label>
                  <span>{{ currentPet.breed }}</span>
                </div>
                <div class="detail-item">
                  <label>性别:</label>
                  <span>{{ currentPet.gender }}</span>
                </div>
                <div class="detail-item">
                  <label>年龄:</label>
                  <span>{{ currentPet.age }}</span>
                </div>
                <div class="detail-item">
                  <label>体重:</label>
                  <span>{{ currentPet.weight }}kg</span>
                </div>
                <div class="detail-item">
                  <label>主人:</label>
                  <span>{{ currentPet.ownerName }}</span>
                </div>
                <div class="detail-item">
                  <label>联系电话:</label>
                  <span>{{ currentPet.ownerPhone }}</span>
                </div>
                <div class="detail-item">
                  <label>备注:</label>
                  <span>{{ currentPet.notes || '无' }}</span>
                </div>
                <div class="detail-item">
                  <label>创建时间:</label>
                  <span>{{ formatDate(currentPet.createTime) }}</span>
                </div>
              </div>
              <div class="modal-footer">
                <button class="close-modal-btn" @click="showDetailModal = false">关闭</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo')))
const pets = ref([])
const searchQuery = ref('')
const filters = ref({
  type: '',
  gender: ''
})
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const showDetailModal = ref(false)
const currentPet = ref({
  name: '',
  type: '',
  breed: '',
  gender: '',
  age: '',
  weight: 0,
  ownerName: '',
  ownerPhone: '',
  notes: '',
  createTime: ''
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  router.push('/admin/login')
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

const handleSearch = () => {
  // 实现搜索逻辑
  fetchPets()
}

const handleFilter = () => {
  // 实现筛选逻辑
  fetchPets()
}

const changePage = (page) => {
  currentPage.value = page
  fetchPets()
}

const viewPetDetail = (petId) => {
  // 实现查看宠物详情逻辑
  const pet = pets.value.find(p => p.id === petId)
  if (pet) {
    currentPet.value = {
      name: pet.name,
      type: pet.type,
      breed: pet.breed || '未知',
      gender: pet.gender,
      age: pet.age,
      weight: pet.weight || 0,
      ownerName: pet.ownerName,
      ownerPhone: pet.ownerPhone || '未知',
      notes: pet.notes || '无',
      createTime: pet.createTime
    }
    showDetailModal.value = true
  }
}

const fetchPets = async () => {
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    // 模拟数据
    pets.value = [
      {
        id: 1,
        name: '小狗狗',
        type: '狗',
        gender: '公',
        age: '2岁',
        ownerName: '用户1',
        createTime: '2026-01-01T00:00:00'
      },
      {
        id: 2,
        name: '小猫咪',
        type: '猫',
        gender: '母',
        age: '1岁',
        ownerName: '用户2',
        createTime: '2026-01-02T00:00:00'
      },
      {
        id: 3,
        name: '小兔子',
        type: '其他',
        gender: '公',
        age: '6个月',
        ownerName: '用户3',
        createTime: '2026-01-03T00:00:00'
      },
      {
        id: 4,
        name: '小仓鼠',
        type: '其他',
        gender: '母',
        age: '3个月',
        ownerName: '用户4',
        createTime: '2026-01-04T00:00:00'
      },
      {
        id: 5,
        name: '小狗狗2',
        type: '狗',
        gender: '母',
        age: '3岁',
        ownerName: '用户5',
        createTime: '2026-01-05T00:00:00'
      }
    ]
    totalPages.value = 1
  } catch (error) {
    console.error('获取宠物列表失败:', error)
  }
}

onMounted(() => {
  fetchPets()
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
  min-width: 150px;
}

/* 宠物列表 */
.pet-list {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.pet-table {
  width: 100%;
  border-collapse: collapse;
}

.pet-table th,
.pet-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #eaecef;
}

.pet-table th {
  background-color: #f6f8fa;
  font-weight: 600;
  font-size: 14px;
  color: #24292f;
}

.pet-table td {
  font-size: 14px;
  color: #57606a;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.detail-btn {
  padding: 6px 12px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.detail-btn:hover {
  background-color: #66b1ff;
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
  max-width: 600px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eaecef;
  position: sticky;
  top: 0;
  background-color: white;
  z-index: 10;
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

.pet-detail {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.detail-item label {
  width: 100px;
  font-weight: 600;
  font-size: 14px;
  color: #24292f;
  flex-shrink: 0;
}

.detail-item span {
  flex: 1;
  font-size: 14px;
  color: #57606a;
  line-height: 1.5;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px;
  border-top: 1px solid #eaecef;
  margin-top: 20px;
  position: sticky;
  bottom: 0;
  background-color: white;
  z-index: 10;
}

.close-modal-btn {
  padding: 8px 16px;
  background-color: white;
  border: 1px solid #d0d7de;
  border-radius: 6px;
  font-size: 14px;
  color: #24292f;
  cursor: pointer;
  transition: all 0.3s;
}

.close-modal-btn:hover {
  background-color: #f6f8fa;
  border-color: #d0d7de;
}
</style>
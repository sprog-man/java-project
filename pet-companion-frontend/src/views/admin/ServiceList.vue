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
            <router-link to="/admin/services" class="nav-item active">
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
          <h3>服务管理</h3>
        </div>
        <div class="top-nav-right">
          <span class="user-info">欢迎，{{ userInfo?.nickname || userInfo?.username }}</span>
          <button class="logout-btn" @click="handleLogout">退出登录</button>
        </div>
      </header>
      
      <!-- 内容区 -->
      <div class="content">
        <!-- 搜索和添加 -->
        <div class="search-add">
          <div class="search-box">
            <input 
              type="text" 
              v-model="searchQuery" 
              placeholder="搜索服务名称"
              @input="handleSearch"
            />
            <button class="search-btn">搜索</button>
          </div>
          <button class="add-btn" @click="showAddModal = true">添加服务</button>
        </div>
        
        <!-- 服务列表 -->
        <div class="service-list">
          <table class="service-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>服务名称</th>
                <th>服务描述</th>
                <th>价格</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="service in services" :key="service.id">
                <td>{{ service.id }}</td>
                <td>{{ service.name }}</td>
                <td>{{ service.description }}</td>
                <td>¥{{ service.price.toFixed(2) }}</td>
                <td>{{ getServiceStatusText(service.status) }}</td>
                <td>{{ formatDate(service.createTime) }}</td>
                <td class="action-buttons">
                  <button class="edit-btn" @click="editService(service)">编辑</button>
                  <button class="delete-btn" @click="deleteService(service.id)">删除</button>
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
        
        <!-- 添加服务对话框 -->
        <div class="modal" v-if="showAddModal">
          <div class="modal-content">
            <div class="modal-header">
              <h3>添加服务</h3>
              <button class="close-btn" @click="showAddModal = false">&times;</button>
            </div>
            <div class="modal-body">
              <form @submit.prevent="submitAdd">
                <div class="form-group">
                  <label>服务名称</label>
                  <input type="text" v-model="addForm.name" required />
                </div>
                <div class="form-group">
                  <label>服务描述</label>
                  <textarea v-model="addForm.description" rows="4"></textarea>
                </div>
                <div class="form-group">
                  <label>价格</label>
                  <input type="number" v-model="addForm.price" step="0.01" min="0" required />
                </div>
                <div class="form-group">
                  <label>状态</label>
                  <select v-model="addForm.status">
                    <option value="0">启用</option>
                    <option value="1">禁用</option>
                  </select>
                </div>
                <div class="modal-footer">
                  <button type="button" class="cancel-btn" @click="showAddModal = false">取消</button>
                  <button type="submit" class="save-btn">保存</button>
                </div>
              </form>
            </div>
          </div>
        </div>
        
        <!-- 编辑服务对话框 -->
        <div class="modal" v-if="showEditModal">
          <div class="modal-content">
            <div class="modal-header">
              <h3>编辑服务</h3>
              <button class="close-btn" @click="showEditModal = false">&times;</button>
            </div>
            <div class="modal-body">
              <form @submit.prevent="submitEdit">
                <div class="form-group">
                  <label>服务名称</label>
                  <input type="text" v-model="editForm.name" required />
                </div>
                <div class="form-group">
                  <label>服务描述</label>
                  <textarea v-model="editForm.description" rows="4"></textarea>
                </div>
                <div class="form-group">
                  <label>价格</label>
                  <input type="number" v-model="editForm.price" step="0.01" min="0" required />
                </div>
                <div class="form-group">
                  <label>状态</label>
                  <select v-model="editForm.status">
                    <option value="0">启用</option>
                    <option value="1">禁用</option>
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo')))
const services = ref([])
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const showAddModal = ref(false)
const showEditModal = ref(false)
const addForm = ref({
  name: '',
  description: '',
  price: 0,
  status: 0
})
const editForm = ref({
  id: '',
  name: '',
  description: '',
  price: 0,
  status: 0
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  router.push('/admin/login')
}

const getServiceStatusText = (status) => {
  return status === 0 ? '启用' : '禁用'
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

const handleSearch = () => {
  // 实现搜索逻辑
  fetchServices()
}

const changePage = (page) => {
  currentPage.value = page
  fetchServices()
}

const editService = (service) => {
  editForm.value = {
    id: service.id,
    name: service.name,
    description: service.description,
    price: service.price,
    status: service.status
  }
  showEditModal.value = true
}

const submitEdit = async () => {
  try {
    // 实现编辑服务逻辑
    console.log('编辑服务:', editForm.value)
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    showEditModal.value = false
    fetchServices()
  } catch (error) {
    console.error('编辑服务失败:', error)
  }
}

const submitAdd = async () => {
  try {
    // 实现添加服务逻辑
    console.log('添加服务:', addForm.value)
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    showAddModal.value = false
    // 重置表单
    addForm.value = {
      name: '',
      description: '',
      price: 0,
      status: 0
    }
    fetchServices()
  } catch (error) {
    console.error('添加服务失败:', error)
  }
}

const deleteService = async (serviceId) => {
  if (!confirm('确定要删除这个服务吗？')) return
  try {
    // 实现删除服务逻辑
    console.log('删除服务:', serviceId)
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    fetchServices()
  } catch (error) {
    console.error('删除服务失败:', error)
  }
}

const fetchServices = async () => {
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    // 模拟数据
    services.value = [
      {
        id: 1,
        name: '宠物洗澡',
        description: '专业宠物洗澡服务，包括梳理毛发、清洁耳道、修剪指甲等',
        price: 50.00,
        status: 0,
        createTime: '2026-01-01T00:00:00'
      },
      {
        id: 2,
        name: '宠物寄养',
        description: '提供宠物寄养服务，包括每日喂食、遛弯、玩耍等',
        price: 100.00,
        status: 0,
        createTime: '2026-01-02T00:00:00'
      },
      {
        id: 3,
        name: '宠物美容',
        description: '专业宠物美容服务，包括洗澡、修剪毛发、造型设计等',
        price: 150.00,
        status: 0,
        createTime: '2026-01-03T00:00:00'
      },
      {
        id: 4,
        name: '宠物训练',
        description: '专业宠物训练服务，包括基础 obedience训练、行为纠正等',
        price: 200.00,
        status: 1,
        createTime: '2026-01-04T00:00:00'
      },
      {
        id: 5,
        name: '宠物医疗',
        description: '提供宠物医疗服务，包括常规检查、疫苗接种、疾病治疗等',
        price: 300.00,
        status: 0,
        createTime: '2026-01-05T00:00:00'
      }
    ]
    totalPages.value = 1
  } catch (error) {
    console.error('获取服务列表失败:', error)
  }
}

onMounted(() => {
  fetchServices()
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

/* 搜索和添加 */
.search-add {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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

.add-btn {
  padding: 8px 16px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.add-btn:hover {
  background-color: #66b1ff;
}

/* 服务列表 */
.service-list {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.service-table {
  width: 100%;
  border-collapse: collapse;
}

.service-table th,
.service-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #eaecef;
}

.service-table th {
  background-color: #f6f8fa;
  font-weight: 600;
  font-size: 14px;
  color: #24292f;
}

.service-table td {
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
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d0d7de;
  border-radius: 6px;
  font-size: 14px;
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
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
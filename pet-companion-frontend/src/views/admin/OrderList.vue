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
            <router-link to="/admin/orders" class="nav-item active">
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
          <h3>订单管理</h3>
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
              placeholder="搜索订单号或用户名"
              @input="handleSearch"
            />
            <button class="search-btn">搜索</button>
          </div>
          <div class="filter-box">
            <select v-model="filters.status" @change="handleFilter">
              <option value="">全部状态</option>
              <option value="PENDING_PAYMENT">待支付</option>
              <option value="PENDING_ACCEPT">待接单</option>
              <option value="IN_SERVICE">服务中</option>
              <option value="COMPLETED">已完成</option>
              <option value="CANCELLED">已取消</option>
            </select>
            <select v-model="filters.serviceType" @change="handleFilter">
              <option value="">全部服务类型</option>
              <option value="1">宠物洗澡</option>
              <option value="2">宠物寄养</option>
              <option value="3">宠物美容</option>
              <option value="4">宠物训练</option>
              <option value="5">宠物医疗</option>
            </select>
          </div>
        </div>
        
        <!-- 订单列表 -->
        <div class="order-list">
          <table class="order-table">
            <thead>
              <tr>
                <th>订单号</th>
                <th>用户</th>
                <th>服务类型</th>
                <th>服务时间</th>
                <th>金额</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in orders" :key="order.id">
                <td>{{ order.orderNumber }}</td>
                <td>{{ order.userName }}</td>
                <td>{{ order.serviceName }}</td>
                <td>{{ order.serviceTime }}</td>
                <td>¥{{ order.amount.toFixed(2) }}</td>
                <td>{{ getOrderStatusText(order.status) }}</td>
                <td>{{ formatDate(order.createTime) }}</td>
                <td class="action-buttons">
                  <button class="detail-btn" @click="viewOrderDetail(order.id)">详情</button>
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
        
        <!-- 订单详情对话框 -->
        <div class="modal" v-if="showDetailModal">
          <div class="modal-content">
            <div class="modal-header">
              <h3>订单详情</h3>
              <button class="close-btn" @click="showDetailModal = false">&times;</button>
            </div>
            <div class="modal-body">
              <div class="order-detail">
                <div class="detail-item">
                  <label>订单号:</label>
                  <span>{{ currentOrder.orderNumber }}</span>
                </div>
                <div class="detail-item">
                  <label>用户:</label>
                  <span>{{ currentOrder.userName }}</span>
                </div>
                <div class="detail-item">
                  <label>服务类型:</label>
                  <span>{{ currentOrder.serviceName }}</span>
                </div>
                <div class="detail-item">
                  <label>服务时间:</label>
                  <span>{{ currentOrder.serviceTime }}</span>
                </div>
                <div class="detail-item">
                  <label>服务地址:</label>
                  <span>{{ currentOrder.serviceAddress }}</span>
                </div>
                <div class="detail-item">
                  <label>联系电话:</label>
                  <span>{{ currentOrder.phone }}</span>
                </div>
                <div class="detail-item">
                  <label>备注:</label>
                  <span>{{ currentOrder.notes || '无' }}</span>
                </div>
                <div class="detail-item">
                  <label>宠物信息:</label>
                  <span>{{ currentOrder.petName }} ({{ currentOrder.petType }})</span>
                </div>
                <div class="detail-item">
                  <label>金额:</label>
                  <span>¥{{ currentOrder.amount.toFixed(2) }}</span>
                </div>
                <div class="detail-item">
                  <label>状态:</label>
                  <span>{{ getOrderStatusText(currentOrder.status) }}</span>
                </div>
                <div class="detail-item">
                  <label>创建时间:</label>
                  <span>{{ formatDate(currentOrder.createTime) }}</span>
                </div>
                <div class="detail-item">
                  <label>更新时间:</label>
                  <span>{{ formatDate(currentOrder.updateTime) }}</span>
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
const orders = ref([])
const searchQuery = ref('')
const filters = ref({
  status: '',
  serviceType: ''
})
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const showDetailModal = ref(false)
const currentOrder = ref({
  orderNumber: '',
  userName: '',
  serviceName: '',
  serviceTime: '',
  serviceAddress: '',
  phone: '',
  notes: '',
  petName: '',
  petType: '',
  amount: 0,
  status: '',
  createTime: '',
  updateTime: ''
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  router.push('/admin/login')
}

const getOrderStatusText = (status) => {
  const statusMap = {
    'PENDING_PAYMENT': '待支付',
    'PENDING_ACCEPT': '待接单',
    'IN_SERVICE': '服务中',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return statusMap[status] || '未知'
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

const handleSearch = () => {
  // 实现搜索逻辑
  fetchOrders()
}

const handleFilter = () => {
  // 实现筛选逻辑
  fetchOrders()
}

const changePage = (page) => {
  currentPage.value = page
  fetchOrders()
}

const viewOrderDetail = (orderId) => {
  // 实现查看订单详情逻辑
  const order = orders.value.find(o => o.id === orderId)
  if (order) {
    currentOrder.value = {
      orderNumber: order.orderNumber,
      userName: order.userName,
      serviceName: order.serviceName,
      serviceTime: order.serviceTime,
      serviceAddress: order.serviceAddress || '北京市朝阳区',
      phone: order.phone || '13800138000',
      notes: order.notes || '无',
      petName: order.petName || '小狗狗',
      petType: order.petType || '狗',
      amount: order.amount,
      status: order.status,
      createTime: order.createTime,
      updateTime: order.updateTime || order.createTime
    }
    showDetailModal.value = true
  }
}

const fetchOrders = async () => {
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    // 模拟数据
    orders.value = [
      {
        id: 1,
        orderNumber: '202604080001',
        userName: '用户1',
        serviceName: '宠物洗澡',
        serviceTime: '2026-04-10 10:00',
        amount: 50.00,
        status: 'PENDING_PAYMENT',
        createTime: '2026-04-08T08:00:00'
      },
      {
        id: 2,
        orderNumber: '202604080002',
        userName: '用户2',
        serviceName: '宠物寄养',
        serviceTime: '2026-04-11 09:00',
        amount: 100.00,
        status: 'PENDING_ACCEPT',
        createTime: '2026-04-08T09:00:00'
      },
      {
        id: 3,
        orderNumber: '202604080003',
        userName: '用户3',
        serviceName: '宠物美容',
        serviceTime: '2026-04-09 14:00',
        amount: 150.00,
        status: 'IN_SERVICE',
        createTime: '2026-04-08T10:00:00'
      },
      {
        id: 4,
        orderNumber: '202604080004',
        userName: '用户4',
        serviceName: '宠物医疗',
        serviceTime: '2026-04-08 16:00',
        amount: 300.00,
        status: 'COMPLETED',
        createTime: '2026-04-08T11:00:00'
      },
      {
        id: 5,
        orderNumber: '202604080005',
        userName: '用户5',
        serviceName: '宠物训练',
        serviceTime: '2026-04-12 10:00',
        amount: 200.00,
        status: 'CANCELLED',
        createTime: '2026-04-08T12:00:00'
      }
    ]
    totalPages.value = 1
  } catch (error) {
    console.error('获取订单列表失败:', error)
  }
}

onMounted(() => {
  fetchOrders()
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

/* 订单列表 */
.order-list {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.order-table {
  width: 100%;
  border-collapse: collapse;
}

.order-table th,
.order-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #eaecef;
}

.order-table th {
  background-color: #f6f8fa;
  font-weight: 600;
  font-size: 14px;
  color: #24292f;
}

.order-table td {
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

.order-detail {
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
  width: 120px;
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
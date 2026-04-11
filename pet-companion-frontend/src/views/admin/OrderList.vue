<template>
  <div class="order-list-container">
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
          <td>{{ order.orderNo }}</td>
          <!-- ✅ 使用新增加的 userName 字段 -->
          <td>{{ order.userName || '未知用户' }}</td>
          <td>{{ order.serviceName }}</td>
          <td>{{ order.serviceTime }}</td>
          <td>¥{{ order.price ? Number(order.price).toFixed(2) : '0.00' }}</td>
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
</template>

<script setup>import { ref, onMounted } from 'vue'
import { useAdminStore } from '../../store/admin' // ✅ 引入 store


const adminStore = useAdminStore()
const orders = ref([])
const currentPage = ref(1)
const totalPages = ref(1)
const searchQuery = ref('')
const filters = ref({ status: '' })

const pageSize = ref(10)
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
  currentPage.value = 1
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

// ✅ 真实获取数据
const fetchOrders = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: 10,
      status: filters.value.status || undefined,
      keyword: searchQuery.value || undefined
    }

    const pageData = await adminStore.fetchOrderList(params)
    orders.value = pageData.records || []
    totalPages.value = pageData.pages || 1
  } catch (error) {
    console.error('获取订单失败', error)
  }
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>

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
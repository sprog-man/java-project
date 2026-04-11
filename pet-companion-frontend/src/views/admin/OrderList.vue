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
        <!-- ✅ 1. 动态渲染状态筛选 -->
        <select v-model="filters.status" @change="handleFilter">
          <option v-for="opt in adminStore.orderStatusOptions" :key="opt.value" :value="opt.value">
            {{ opt.label }}
          </option>
        </select>
        <!-- ✅ 2. 动态渲染服务类型筛选 -->
        <select v-model="filters.serviceTypeId" @change="handleFilter">
          <option value="">全部服务类型</option>
          <option v-for="type in adminStore.serviceList" :key="type.id" :value="type.id">
            {{ type.name }}
          </option>
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
        <!-- ✅ 3. 确保 orders 有数据再渲染 -->
        <tr v-for="order in orders" :key="order.id">
          <td>{{ order.orderNo }}</td>
          <td>{{ order.userName || '未知用户' }}</td>
          <td>{{ order.serviceName }}</td>
          <td>{{ order.serviceTime }}</td>
          <td>¥{{ order.price ? Number(order.price).toFixed(2) : '0.00' }}</td>
          <td>{{ order.statusText }}</td>
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
            <!-- ✅ 4. 绑定 currentOrder 的真实字段 -->
            <div class="detail-item"><label>订单号:</label><span>{{ currentOrder.orderNo }}</span></div>
            <div class="detail-item"><label>用户:</label><span>{{ currentOrder.userName }}</span></div>
            <div class="detail-item"><label>服务类型:</label><span>{{ currentOrder.serviceName }}</span></div>
            <div class="detail-item"><label>服务时间:</label><span>{{ currentOrder.serviceTime }}</span></div>
            <div class="detail-item"><label>服务地址:</label><span>{{ currentOrder.address }}</span></div>
            <div class="detail-item"><label>联系电话:</label><span>{{ currentOrder.phone }}</span></div>
            <div class="detail-item"><label>备注:</label><span>{{ currentOrder.notes || '无' }}</span></div>
            <div class="detail-item"><label>宠物名称:</label><span>{{ currentOrder.petName }}</span></div>
            <div class="detail-item"><label>金额:</label><span>¥{{ currentOrder.price }}</span></div>
            <div class="detail-item"><label>状态:</label><span>{{ currentOrder.statusText }}</span></div>
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
const filters = ref({ status: '', serviceTypeId: '' }) // ✅ 增加 serviceTypeId


const pageSize = ref(10)
const showDetailModal = ref(false)
const currentOrder = ref({}) // ✅ 简化初始值

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
  currentPage.value = 1
  fetchOrders()
}

const changePage = (page) => {
  currentPage.value = page
  fetchOrders()
}

// ✅ 5. 修复详情查看逻辑
const viewOrderDetail = async (orderId) => {
  try {
    // 方案 A：直接从当前列表里找（速度快）
    const order = orders.value.find(o => o.id === orderId)
    if (order) {
      currentOrder.value = order
      showDetailModal.value = true
    }

    // 方案 B（可选）：如果列表里的信息不全，可以再次请求后端详情接口
    // const res = await axios.get(`/admin/orders/${orderId}`)
    // currentOrder.value = res.data.data
  } catch (error) {
    console.error('获取订单详情失败', error)
  }
}

// ✅ 真实获取数据
const fetchOrders = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: 10,
      status: filters.value.status || undefined,
      serviceTypeId: filters.value.serviceTypeId || undefined, // ✅ 关键：把服务类型ID传给后端
      keyword: searchQuery.value || undefined
    }
    console.log('正在发送筛选请求:', params) // ✅ 调试用：看看参数对不对
    const pageData = await adminStore.fetchOrderList(params)
    orders.value = pageData.records || []
    totalPages.value = pageData.pages || 1
  } catch (error) {
    console.error('获取订单失败', error)
  }
}

onMounted(async () => {
  // ✅ 6. 页面加载时并行获取所有必要数据
  await Promise.all([
    adminStore.fetchServiceList(), // 获取服务类型用于筛选
    adminStore.fetchOrderStatusOptions(), // 获取状态选项用于筛选
    fetchOrders() // 获取订单列表
  ])
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
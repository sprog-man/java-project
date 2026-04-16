<template>
  <div class="review-list-container">
    <!-- 搜索和筛选 -->
    <div class="search-filter">
      <div class="search-box">
        <input
            type="text"
            v-model="searchQuery"
            placeholder="搜索订单号或用户名"
            @keyup.enter="handleSearch"
        />
        <button class="search-btn" @click="handleSearch">搜索</button>
      </div>
      <div class="filter-box">
        <select v-model="filters.rating" @change="handleFilter">
          <option value="">全部评分</option>
          <option value="5">5星</option>
          <option value="4">4星</option>
          <option value="3">3星</option>
          <option value="2">2星</option>
          <option value="1">1星</option>
        </select>
      </div>
    </div>

    <!-- 评价列表 -->
    <div class="review-list">
      <table class="review-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>订单号</th>
          <th>用户</th>
          <th>服务类型</th>
          <th>评分</th>
          <th>评价内容</th>
          <th>创建时间</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="review in reviews" :key="review.id">
          <td>{{ review.id }}</td>
          <td>{{ review.orderNo }}</td>
          <td>{{ review.userName }}</td>
          <td>{{ review.serviceName }}</td>
          <td>{{ '⭐'.repeat(review.score) }}</td>
          <td class="review-content">{{ review.content }}</td>
          <td>{{ review.createTime }}</td>
          <td class="action-buttons">
            <button class="detail-btn" @click="viewReviewDetail(review.id)">详情</button>
          </td>
        </tr>
        </tbody>
      </table>

      <!-- 分页 -->
      <div class="pagination">
        <button class="page-btn" :disabled="currentPage === 1" @click="changePage(currentPage - 1)">上一页</button>
        <span class="page-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
        <button class="page-btn" :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)">下一页</button>
      </div>
    </div>

    <!-- 评价详情对话框 (保持不变，只需确保字段名对应即可) -->
    <div class="modal" v-if="showDetailModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>评价详情</h3>
          <button class="close-btn" @click="showDetailModal = false">&times;</button>
        </div>
        <div class="modal-body">
          <div class="review-detail">
            <div class="detail-item"><label>订单号:</label><span>{{ currentReview.orderNo }}</span></div>
            <div class="detail-item"><label>用户:</label><span>{{ currentReview.userName }}</span></div>
            <div class="detail-item"><label>服务类型:</label><span>{{ currentReview.serviceName }}</span></div>
            <div class="detail-item"><label>评分:</label><span>{{ '⭐'.repeat(currentReview.score) }}</span></div>
            <div class="detail-item"><label>评价内容:</label><span>{{ currentReview.content }}</span></div>
            <div class="detail-item"><label>创建时间:</label><span>{{ currentReview.createTime }}</span></div>
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
import { useAdminStore } from '../../store/admin' // ✅ 引入 Store


const adminStore = useAdminStore()
const reviews = ref([])
const searchQuery = ref('')
const filters = ref({ rating: '' })
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const showDetailModal = ref(false)
const currentReview = ref({})

const getRatingStars = (rating) => {
  return '⭐'.repeat(rating)
}


const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

// ✅ 真实获取数据
const fetchReviews = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value,
      rating: filters.value.rating || null
    }
    const pageData = await adminStore.fetchReviewList(params)
    reviews.value = pageData.records || []
    totalPages.value = Math.ceil(pageData.total / pageSize.value) || 1
  } catch (error) {
    console.error('获取评价列表失败:', error)
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchReviews()
}

const handleFilter = () => {
  currentPage.value = 1
  fetchReviews()
}

const changePage = (page) => {
  currentPage.value = page
  fetchReviews()
}

const viewReviewDetail = (reviewId) => {
  const review = reviews.value.find(r => r.id === reviewId)
  if (review) {
    currentReview.value = review
    showDetailModal.value = true
  }
}

onMounted(() => {
  fetchReviews()
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

/* 评价列表 */
.review-list {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.review-table {
  width: 100%;
  border-collapse: collapse;
}

.review-table th,
.review-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #eaecef;
}

.review-table th {
  background-color: #f6f8fa;
  font-weight: 600;
  font-size: 14px;
  color: #24292f;
}

.review-table td {
  font-size: 14px;
  color: #57606a;
}

.review-content {
  max-width: 300px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
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

.review-detail {
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
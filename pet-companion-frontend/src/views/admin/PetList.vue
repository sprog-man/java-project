<template>
  <div class="pet-list-container">
    <!-- 搜索和筛选 -->
    <div class="search-filter">
      <div class="search-box">
        <input
            type="text"
            v-model="searchQuery"
            placeholder="搜索宠物名称或主人"
            @input="handleSearch"
        />
        <button class="search-btn">搜索</button>
      </div>
      <div class="filter-box">
        <!-- ✅ 1. 动态渲染类型筛选 -->
        <select v-model="filters.type" @change="handleFilter">
          <option v-for="opt in adminStore.petTypeOptions" :key="opt.value" :value="opt.value">
            {{ opt.label }}
          </option>
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
          <th>主人</th> <!-- ✅ 对应 ownerName -->
          <th>创建时间</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="pet in pets" :key="pet.id">
          <td>{{ pet.id }}</td>
          <td>{{ pet.name }}</td>
          <td>{{ pet.typeName }}</td> <!-- ✅ 使用 typeName -->
          <td>{{ pet.genderName }}</td> <!-- ✅ 使用 genderName -->
          <td>{{ pet.age }}岁</td>
          <td>{{ pet.ownerName || '未知' }}</td> <!-- ✅ 使用 ownerName -->
          <td>{{ formatDate(pet.createTime) }}</td>
          <td class="action-buttons">
            <button class="detail-btn" @click="viewPetDetail(pet)">详情</button>
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
              <span>{{ currentPet.typeName }}</span>
            </div>
            <div class="detail-item">
              <label>品种:</label>
              <span>{{ currentPet.breed }}</span>
            </div>
            <div class="detail-item">
              <label>性别:</label>
              <span>{{ currentPet.genderName }}</span>
            </div>
            <div class="detail-item">
              <label>年龄:</label>
              <span>{{ currentPet.age }} 岁</span>
            </div>
            <div class="detail-item">
              <label>体重:</label>
              <span>{{ currentPet.weight }} kg</span>
            </div>
            <div class="detail-item">
              <label>主人:</label>
              <span>{{ currentPet.ownerName }}</span>
            </div>
            <div class="detail-item">
              <label>描述:</label>
              <span>{{ currentPet.description || '无' }}</span>
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
</template>

<script setup>import { ref, onMounted } from 'vue'
import { useAdminStore } from '../../store/admin' // ✅ 引入 store

const adminStore = useAdminStore()
const pets = ref([])
const searchQuery = ref('')
const filters = ref({ type: '' }) // ✅ 2. 简化 filters，只保留 type

const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const showDetailModal = ref(false)
const currentPet = ref({})


const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

const handleSearch = () => {
  currentPage.value = 1
  fetchPets()
}

const handleFilter = () => {
  currentPage.value = 1
  fetchPets()
}

const changePage = (page) => {
  currentPage.value = page
  fetchPets()
}

const viewPetDetail = (pet) => {
  currentPet.value = pet
  showDetailModal.value = true
}

const fetchPets = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value,
      type: filters.value.type || undefined // ✅ 3. 关键：把 type 传给后端
    }

    console.log('正在请求宠物列表:', params) // 调试用
    const pageData = await adminStore.fetchPetList(params)

    pets.value = pageData.records || []
    totalPages.value = Math.ceil(pageData.total / pageSize.value) || 1

  } catch (error) {
    console.error('获取宠物列表失败:', error)
  }
}

onMounted(() => {
  fetchPets()
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
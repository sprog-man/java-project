<template>
  <div class="service-list-container">
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
          <!-- Deleted:<th>状态</th> -->
          <th>创建时间</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <!-- ✅ 使用过滤后的列表 -->
        <tr v-for="service in filteredServices" :key="service.id">
          <td>{{ service.id }}</td>
          <td>{{ service.name }}</td>
          <td>{{ service.description }}</td>
          <td>¥{{ Number(service.price).toFixed(2) }}</td>
          <!-- Deleted:<td>{{ getServiceStatusText(service.status) }}</td> -->
          <td>{{ formatDate(service.createTime) }}</td>
          <td class="action-buttons">
            <button class="edit-btn" @click="editService(service)">编辑</button>
            <button class="delete-btn" @click="deleteService(service.id)">删除</button>
          </td>
        </tr>
        </tbody>
      </table>

      <!-- 分页 (目前仅占位，防止报错) -->
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
              <!-- ✅ 修改：绑定到 price -->
              <input type="number" v-model="addForm.price" step="0.01" min="0" required />
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
              <!-- ✅ 修改：绑定到 price -->
              <input type="number" v-model="editForm.price" step="0.01" min="0" required />
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
</template>


<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useAdminStore } from '../../store/admin'

const route = useRoute()
const adminStore = useAdminStore()

// ✅ 1. 绑定到 store 的状态
const services = computed(() => adminStore.serviceList || [])
const searchQuery = ref('')
const showAddModal = ref(false)
const showEditModal = ref(false)

// ✅ 2. 补全缺失的分页变量
const currentPage = ref(1)
const totalPages = ref(1)

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

// ✅ 3. 核心修复：监听路由变化，确保跳转逻辑生效
watch(() => route.path, (newPath, oldPath) => {
  // 当路由发生变化时，如果离开了当前页面，可以做一些清理工作
  // 如果是进入了当前页面，确保数据加载
  if (newPath === '/admin/services' && oldPath !== newPath) {
    fetchServices()
  }
})




const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

// ✅ 3. 前端搜索过滤
const filteredServices = computed(() => {
  if (!searchQuery.value) return services.value
  return services.value.filter(s => s.name && s.name.includes(searchQuery.value))
})

const handleSearch = () => {
  // 搜索逻辑由 computed 属性自动处理
}

// ✅ 4. 补全缺失的分页函数
const changePage = (page) => {
  currentPage.value = page
}

const editService = (service) => {
  editForm.value = {
    id: service.id,
    name: service.name,
    description: service.description,
    price: service.price, // ✅ 确保这里是 price
    status: service.status
  }
  showEditModal.value = true
}

const submitEdit = async () => {
  try {
    await adminStore.updateService(editForm.value.id, editForm.value)
    alert('服务更新成功！')
    showEditModal.value = false
  } catch (error) {
    console.error('编辑服务失败:', error)
    alert('更新失败: ' + (error.message || '未知错误'))
  }
}

const submitAdd = async () => {
  try {
    await adminStore.addService(addForm.value)
    alert('服务添加成功！')
    showAddModal.value = false
    addForm.value = { name: '', description: '', price: 0, status: 0 }
  } catch (error) {
    console.error('添加服务失败:', error)
    alert('添加失败: ' + (error.message || '未知错误'))
  }
}

const deleteService = async (serviceId) => {
  if (!confirm('确定要删除这个服务吗？')) return
  try {
    await adminStore.deleteService(serviceId)
    alert('服务已删除')
  } catch (error) {
    console.error('删除服务失败:', error)
    alert('删除失败: ' + (error.message || '未知错误'))
  }
}

const fetchServices = async () => {
  try {
    await adminStore.fetchServiceList()
  } catch (error) {
    console.error('获取服务列表失败:', error)
  }
}

onMounted(() => {
  fetchServices()
})
</script>

<style scoped>
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
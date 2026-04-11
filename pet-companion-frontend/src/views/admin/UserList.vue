<template>
  <div>
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
          <h3>编辑用户状态</h3>
          <button class="close-btn" @click="showEditModal = false">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitEdit">
            <div class="form-group">
              <label>用户名</label>
              <input type="text" :value="editForm.username" disabled />
            </div>
            <div class="form-group">
              <label>手机号</label>
              <input type="text" :value="editForm.phone" disabled />
            </div>

            <!-- ✅ 仅允许管理员修改以下字段 -->
            <div class="form-group">
              <label>账号状态</label>
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
              <label>角色权限</label>
              <select v-model="editForm.role">
                <option value="0">普通用户</option>
                <option value="1">管理员</option>
              </select>
            </div>
            <!-- ✅ 结束 -->
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

<script setup>import { ref, onMounted } from 'vue'
import { useAdminStore } from '../../store/admin'

const adminStore = useAdminStore()

// ✅ 1. 统一声明状态变量（删除重复项）
const users = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const showEditModal = ref(false)

// 搜索和筛选条件
const searchQuery = ref('')
const filters = ref({
  userType: '',
  status: '',
  verified: '',
  role: ''
})

// 编辑表单数据
const editForm = ref({
  id: '',
  username: '', // 仅用于展示
  phone: '',    // 仅用于展示
  status: 0,
  verified: 0,
  role: 0
})

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
  currentPage.value = 1 // 搜索时重置到第一页
  fetchUsers()
}

const handleFilter = () => {
  currentPage.value = 1 // 筛选时重置到第一页
  fetchUsers()
}

const changePage = (page) => {
  currentPage.value = page
  fetchUsers()
}

const editUser = (user) => {
  // ✅ 只提取需要编辑的字段
  editForm.value = {
    id: user.id,
    username: user.username,
    phone: user.phone,
    status: user.status,
    verified: user.verified,
    role: user.role
  }
  showEditModal.value = true
}

const submitEdit = async () => {
  try {
    // ✅ 构造只包含允许修改字段的对象
    const updates = {
      status: Number(editForm.value.status),
      verified: Number(editForm.value.verified),
      role: Number(editForm.value.role)
    }

    // ✅ 调用 Store 的更新方法
    await adminStore.updateUserStatus(editForm.value.id, updates)

    alert('用户状态更新成功！')
    showEditModal.value = false
    fetchUsers() // 刷新列表以显示最新状态
  } catch (error) {
    console.error('编辑用户失败:', error)
    alert('更新失败: ' + (error.message || '未知错误'))
  }
}

const deleteUser = async (userId) => {
  if (!confirm('确定要删除这个用户吗？此操作将永久隐藏该用户。')) return
  try {
    // ✅ 调用 Store 的删除方法
    await adminStore.deleteUser(userId)

    alert('用户已删除')
    fetchUsers() // 刷新列表
  } catch (error) {
    console.error('删除用户失败:', error)
    alert('删除失败: ' + (error.message || '未知错误'))
  }
}

// ✅ 核心：从 Store 获取真实数据
// ✅ 核心：从 Store 获取真实数据并处理筛选
const fetchUsers = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value,
      // ✅ 关键：如果筛选框选了值就传，没选（空字符串）就传 undefined，这样 Axios 就不会把它拼接到 URL 里
      userType: filters.value.userType || undefined,
      status: filters.value.status || undefined,
      verified: filters.value.verified || undefined,
      role: filters.value.role || undefined
    }

    console.log('正在发送筛选请求:', params) // 调试用
    const pageData = await adminStore.fetchUserList(params)
    users.value = pageData.records
    totalPages.value = Math.ceil(pageData.total / pageSize.value)
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

onMounted(() => {
  fetchUsers()
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
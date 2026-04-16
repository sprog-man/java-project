<template>
  <div class="audit-list">
    <h2>服务者申请审核</h2>
    <div class="table-container">
      <table v-if="pendingList.length > 0">
        <thead>
        <tr>
          <th>ID</th>
          <th>真实姓名</th>
          <th>身份证号</th>
          <th>服务经验</th>
          <th>申请时间</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in pendingList" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.realName }}</td>
          <td>{{ item.idCard }}</td>
          <td class="experience-cell">{{ item.experience || '暂无描述' }}</td>
          <td>{{ item.createTime }}</td>
          <td>
            <button class="btn-pass" @click="handleAudit(item.id, 1)">通过</button>
            <button class="btn-reject" @click="handleAudit(item.id, 2)">拒绝</button>
          </td>
        </tr>
        </tbody>
      </table>
      <p v-else class="empty-text">暂无待审核的申请 🎉</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAdminStore } from '../../store/admin'

const adminStore = useAdminStore()
const pendingList = ref([])

const loadList = async () => {
  pendingList.value = await adminStore.fetchPendingProviders()
}

const handleAudit = async (id, status) => {
  const msg = status === 1 ? '确定通过该服务者申请吗？通过后用户将获得服务者权限。' : '确定拒绝该申请吗？'
  if (!confirm(msg)) return

  try {
    await adminStore.auditProvider(id, status)
    alert('审核操作成功')
    loadList() // 刷新列表
  } catch (error) {
    alert('操作失败：' + (error.response?.data?.message || error.message))
  }
}

onMounted(loadList)
</script>

<style scoped>
.table-container {
  background: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

table { width: 100%;
  border-collapse: collapse;
}

th { background-color: #f6f8fa;
  padding: 12px;
  text-align: left;
  font-weight: 600;
  color: #24292f;
}

td { padding: 12px;
  border-bottom: 1px solid #eaecef;
  color: #57606a;
}

.btn-pass { background: #2ea44f;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 8px;
  transition: 0.2s;
}

.experience-cell {
  max-width: 300px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.btn-pass:hover { background: #2c974b; }

.btn-reject { background: #cb2431;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: 0.2s;
}

.btn-reject:hover { background: #d73a49; }
.empty-text { text-align: center;
  color: #586069;
  padding: 40px;
  font-size: 16px;
}
</style>

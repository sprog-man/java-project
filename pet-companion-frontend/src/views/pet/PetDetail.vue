<template>
  <div class="pet-detail">
    <Header />
    
    <div class="container">
      <div class="pet-info-card" v-if="pet.id">
        <div class="pet-avatar">
          <img :src="pet.photo || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20pet%2C%20friendly%20style&image_size=square'" :alt="pet.name" />
        </div>
        <div class="pet-info">
          <h2>{{ pet.name }}</h2>
          <div class="pet-details">
            <div class="detail-item">
              <span class="detail-label">类型：</span>
              <span class="detail-value">{{ getTypeName(pet.type) }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">品种：</span>
              <span class="detail-value">{{ pet.breed || '暂无' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">年龄：</span>
              <span class="detail-value">{{ pet.age }}岁</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">性别：</span>
              <span class="detail-value">{{ getGenderName(pet.gender) }}</span>
            </div>
            <div class="detail-item" v-if="pet.weight">
              <span class="detail-label">体重：</span>
              <span class="detail-value">{{ pet.weight }}kg</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">描述：</span>
              <span class="detail-value">{{ pet.description || '暂无描述' }}</span>
            </div>
          </div>
          <div class="pet-actions">
            <router-link :to="`/user/pets/${pet.id}/edit`" class="btn-primary">编辑</router-link>
            <router-link to="/user/pets" class="btn-outline">返回列表</router-link>
          </div>
        </div>
      </div>
      
      <div v-else class="loading-message">
        <p>加载中...</p>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { usePetStore } from '../../store/pet'

const route = useRoute()
const router = useRouter()
const petStore = usePetStore()
const pet = ref({})

// 获取宠物类型名称
const getTypeName = (type) => {
  const typeMap = {
    1: '猫',
    2: '狗',
    3: '其他'
  }
  return typeMap[type] || '未知'
}

// 获取宠物性别名称
const getGenderName = (gender) => {
  const genderMap = {
    1: '公',
    2: '母'
  }
  return genderMap[gender] || '未知'
}

onMounted(async () => {
  const id = route.params.id
  try {
    // 从后端获取宠物列表
    await petStore.getPetList()
    // 从列表中找到对应的宠物
    const petData = petStore.getPetDetail(id)
    
    if (petData) {
      pet.value = petData
    } else {
      console.error('宠物不存在')
      router.push('/user/pets')
    }
  } catch (error) {
    console.error('获取宠物详情失败', error)
    router.push('/user/pets')
  }
})
</script>

<style scoped>
.pet-detail {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.pet-info-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
  margin-top: var(--spacing-xl);
  margin-bottom: var(--spacing-2xl);
  display: flex;
  gap: var(--spacing-lg);
}

.pet-avatar {
  width: 200px;
  height: 200px;
  border-radius: var(--border-radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  flex-shrink: 0;
}

.pet-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.pet-info {
  flex: 1;
}

.pet-info h2 {
  margin-bottom: var(--spacing-lg);
  color: var(--primary-color);
}

.pet-details {
  margin-bottom: var(--spacing-lg);
}

.detail-item {
  margin-bottom: var(--spacing-sm);
  display: flex;
  align-items: flex-start;
}

.detail-label {
  font-weight: 500;
  color: var(--text-color);
  width: 80px;
  flex-shrink: 0;
}

.detail-value {
  color: var(--light-text-color);
  flex: 1;
}

.pet-actions {
  display: flex;
  gap: var(--spacing-md);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .pet-info-card {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .pet-actions {
    justify-content: center;
  }
}
</style>

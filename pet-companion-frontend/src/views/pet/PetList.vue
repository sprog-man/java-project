<template>
  <div class="pet-list">
    <Header />
    
    <div class="container">
      <div class="page-header">
        <h2 class="page-title">我的宠物</h2>
        <router-link to="/user/pets/add" class="btn-primary">添加宠物</router-link>
      </div>
      
      <div class="pet-grid">
        <div class="pet-card" v-for="pet in pets" :key="pet.id">
          <div class="pet-avatar">
            <img :src="pet.avatar" :alt="pet.name" />
          </div>
          <div class="pet-info">
            <h3>{{ pet.name }}</h3>
            <p>{{ pet.breed }}</p>
            <p>{{ pet.age }}岁</p>
            <div class="pet-actions">
              <router-link :to="`/user/pets/${pet.id}`" class="btn-outline">查看详情</router-link>
              <router-link :to="`/user/pets/${pet.id}/edit`" class="btn-outline">编辑</router-link>
              <button class="btn-outline" @click="deletePet(pet.id)">删除</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { usePetStore } from '../../store/pet'

const petStore = usePetStore()
const pets = ref([])

// 删除宠物
const deletePet = async (id) => {
  if (confirm('确定要删除这个宠物吗？')) {
    try {
      await petStore.deletePet(id)
      // petStore.deletePet已经会重新获取列表，这里只需要更新本地数据
      pets.value = petStore.pets.map(pet => ({
        ...pet,
        avatar: pet.photo || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pet%20avatar%2C%20simple%20style&image_size=square'
      }))
    } catch (error) {
      console.error('删除宠物失败', error)
    }
  }
}

onMounted(async () => {
  try {
    // 从后端获取宠物列表
    const petList = await petStore.getPetList()
    // 处理后端返回的数据，将photo字段映射为avatar
    pets.value = petList.map(pet => ({
      ...pet,
      avatar: pet.photo || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pet%20avatar%2C%20simple%20style&image_size=square'
    }))
  } catch (error) {
    console.error('获取宠物列表失败', error)
  }
})
</script>

<style scoped>
.pet-list {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: var(--spacing-xl);
  margin-bottom: var(--spacing-xl);
}

.page-title {
  font-size: 2rem;
  color: var(--primary-color);
}

.pet-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-2xl);
}

.pet-card {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-fast);
  display: flex;
  gap: var(--spacing-md);
}

.pet-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
}

.pet-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
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
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.pet-info h3 {
  margin-bottom: var(--spacing-xs);
  color: var(--primary-color);
}

.pet-info p {
  margin-bottom: var(--spacing-xs);
  color: var(--light-text-color);
}

.pet-actions {
  display: flex;
  gap: var(--spacing-sm);
  margin-top: var(--spacing-md);
}

.pet-actions .btn-outline {
  padding: var(--spacing-xs) var(--spacing-sm);
  font-size: 0.875rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-md);
  }
  
  .page-title {
    font-size: 1.5rem;
  }
  
  .pet-grid {
    grid-template-columns: 1fr;
  }
  
  .pet-card {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .pet-actions {
    justify-content: center;
  }
}
</style>

<template>
  <div class="edit-pet">
    <Header />
    
    <div class="container">
      <h2 class="page-title">编辑宠物</h2>
      
      <form class="edit-pet-form" @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="name" class="form-label">宠物名称</label>
          <input 
            type="text" 
            id="name" 
            v-model="petForm.name" 
            class="form-input" 
            placeholder="请输入宠物名称"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="type" class="form-label">宠物类型</label>
          <select 
            id="type" 
            v-model="petForm.type" 
            class="form-input"
            required
          >
            <option value="">请选择</option>
            <option :value="1">猫</option>
            <option :value="2">狗</option>
            <option :value="3">其他</option>
          </select>
        </div>
        
        <div class="form-group">
          <label for="breed" class="form-label">宠物品种</label>
          <input 
            type="text" 
            id="breed" 
            v-model="petForm.breed" 
            class="form-input" 
            placeholder="请输入宠物品种"
          />
        </div>
        
        <div class="form-group">
          <label for="age" class="form-label">宠物年龄</label>
          <input 
            type="number" 
            id="age" 
            v-model="petForm.age" 
            class="form-input" 
            placeholder="请输入宠物年龄"
            min="0"
          />
        </div>
        
        <div class="form-group">
          <label for="gender" class="form-label">宠物性别</label>
          <select 
            id="gender" 
            v-model="petForm.gender" 
            class="form-input"
          >
            <option value="">请选择</option>
            <option :value="1">公</option>
            <option :value="2">母</option>
          </select>
        </div>
        
        <div class="form-group">
          <label for="weight" class="form-label">宠物体重(kg)</label>
          <input 
            type="number" 
            id="weight" 
            v-model="petForm.weight" 
            class="form-input" 
            placeholder="请输入宠物体重"
            min="0"
            step="0.1"
          />
        </div>
        
        <div class="form-group">
          <label for="photo" class="form-label">宠物照片</label>
          <input 
            type="file" 
            id="photo" 
            class="form-input" 
            accept="image/*"
            @change="handleFileUpload"
          />
          <div v-if="petForm.photo" class="avatar-preview">
            <img :src="petForm.photo" alt="宠物照片预览" />
          </div>
        </div>
        
        <div class="form-group">
          <label for="description" class="form-label">宠物描述</label>
          <textarea 
            id="description" 
            v-model="petForm.description" 
            class="form-input" 
            placeholder="请输入宠物的性格、习惯等信息"
            rows="4"
          ></textarea>
        </div>
        
        <div class="form-actions">
          <button type="submit" class="btn-primary" :disabled="isLoading">
            <span v-if="isLoading" class="loading"></span>
            <span v-else>保存修改</span>
          </button>
          <router-link :to="`/user/pets/${petId}`" class="btn-outline">取消</router-link>
        </div>
      </form>
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
const petId = route.params.id

// 宠物表单
const petForm = ref({
  name: '',
  type: '',
  breed: '',
  age: '',
  gender: '',
  weight: '',
  photo: '',
  description: ''
})

// 加载状态
const isLoading = ref(false)

// 处理文件上传
const handleFileUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    // 这里应该上传文件到服务器，暂时使用模拟的图片URL
    petForm.value.photo = 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20pet%2C%20friendly%20style&image_size=square'
  }
}

// 处理表单提交
const handleSubmit = async () => {
  isLoading.value = true
  try {
    // 构建符合后端要求的数据
    const petData = {
      name: petForm.value.name,
      type: parseInt(petForm.value.type),
      breed: petForm.value.breed || null,
      age: petForm.value.age ? parseInt(petForm.value.age) : null,
      gender: petForm.value.gender ? parseInt(petForm.value.gender) : null,
      weight: petForm.value.weight ? parseFloat(petForm.value.weight) : null,
      photo: petForm.value.photo || null,
      description: petForm.value.description || null
    }
    
    await petStore.updatePet(petId, petData)
    // 保存成功后跳转到宠物详情
    router.push(`/user/pets/${petId}`)
  } catch (error) {
    console.error('更新宠物信息失败', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(async () => {
  try {
    // 从后端获取宠物列表
    await petStore.getPetList()
    // 从列表中找到对应的宠物
    const pet = petStore.getPetDetail(petId)
    
    if (pet) {
      petForm.value = {
        name: pet.name || '',
        type: pet.type || '',
        breed: pet.breed || '',
        age: pet.age || '',
        gender: pet.gender || '',
        weight: pet.weight || '',
        photo: pet.photo || '',
        description: pet.description || ''
      }
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
.edit-pet {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.page-title {
  font-size: 2rem;
  margin-top: var(--spacing-xl);
  margin-bottom: var(--spacing-xl);
  color: var(--primary-color);
  text-align: center;
}

.edit-pet-form {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--spacing-2xl);
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.form-group {
  margin-bottom: var(--spacing-md);
}

.form-label {
  display: block;
  margin-bottom: var(--spacing-xs);
  font-weight: 500;
  color: var(--text-color);
}

.form-input {
  width: 100%;
  padding: var(--spacing-sm) var(--spacing-md);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-md);
  font-size: 1rem;
  transition: border-color var(--transition-fast);
}

.form-input:focus {
  outline: none;
  border-color: var(--cta-color);
  box-shadow: 0 0 0 3px rgba(3, 105, 161, 0.1);
}

.avatar-preview {
  margin-top: var(--spacing-sm);
  width: 100px;
  height: 100px;
  border-radius: var(--border-radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.form-actions {
  display: flex;
  gap: var(--spacing-md);
  justify-content: center;
  margin-top: var(--spacing-xl);
}

.form-actions .btn-primary,
.form-actions .btn-outline {
  padding: var(--spacing-sm) var(--spacing-xl);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
  }
  
  .edit-pet-form {
    padding: var(--spacing-lg);
    margin: 0 var(--spacing-md) var(--spacing-xl);
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .form-actions .btn-primary,
  .form-actions .btn-outline {
    width: 100%;
  }
}
</style>

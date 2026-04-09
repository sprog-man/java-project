<template>
  <div class="create-order">
    <Header />
    
    <div class="container">
      <h2 class="page-title">预约服务</h2>
      
      <form class="create-order-form" @submit.prevent="handleSubmit">
        <div class="form-section">
          <h3>服务信息</h3>
          
          <div class="form-group">
            <label for="serviceType" class="form-label">服务类型</label>
            <select 
              id="serviceType" 
              v-model="orderForm.serviceType" 
              class="form-input"
              required
            >
              <option value="">请选择</option>
              <option v-for="service in serviceTypes" :key="service.id" :value="service.id">
                {{ service.name }} (¥{{ service.price }}/小时)
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label for="serviceDate" class="form-label">服务日期</label>
            <input 
              type="date" 
              id="serviceDate" 
              v-model="orderForm.serviceDate" 
              class="form-input"
              required
            />
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label for="serviceStartTime" class="form-label">开始时间</label>
              <input 
                type="time" 
                id="serviceStartTime" 
                v-model="orderForm.serviceStartTime" 
                class="form-input"
                required
              />
            </div>
            <div class="form-group">
              <label for="serviceEndTime" class="form-label">结束时间</label>
              <input 
                type="time" 
                id="serviceEndTime" 
                v-model="orderForm.serviceEndTime" 
                class="form-input"
                required
              />
            </div>
          </div>
        </div>
        
        <div class="form-section">
          <h3>宠物信息</h3>
          
          <div class="form-group">
            <label for="pet" class="form-label">选择宠物</label>
            <select 
              id="pet" 
              v-model="orderForm.petId" 
              class="form-input"
              required
            >
              <option value="">请选择</option>
              <option v-for="pet in pets" :key="pet.id" :value="pet.id">
                {{ pet.name }} ({{ pet.breed }})
              </option>
            </select>
            <router-link to="/user/pets/add" class="add-pet-link">添加宠物</router-link>
          </div>
        </div>
        
        <div class="form-section">
          <h3>联系信息</h3>
          
          <div class="form-group">
            <label for="address" class="form-label">服务地址</label>
            <input 
              type="text" 
              id="address" 
              v-model="orderForm.address" 
              class="form-input" 
              placeholder="请输入详细地址"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="phone" class="form-label">联系电话</label>
            <input 
              type="tel" 
              id="phone" 
              v-model="orderForm.phone" 
              class="form-input" 
              placeholder="请输入联系电话"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="notes" class="form-label">备注信息</label>
            <textarea 
              id="notes" 
              v-model="orderForm.notes" 
              class="form-input" 
              placeholder="请输入宠物的特殊需求、注意事项等"
              rows="4"
            ></textarea>
          </div>
        </div>
        
        <div class="form-section">
          <h3>订单金额</h3>
          <div class="order-price">
            <div class="price-item">
              <span class="price-label">服务费用：</span>
              <span class="price-value">¥{{ orderPrice }}</span>
            </div>
            <div class="price-item total">
              <span class="price-label">总计：</span>
              <span class="price-value total">¥{{ orderPrice }}</span>
            </div>
          </div>
        </div>
        
        <div class="form-actions">
          <button type="submit" class="btn-primary" :disabled="isLoading">
            <span v-if="isLoading" class="loading"></span>
            <span v-else>提交订单</span>
          </button>
          <router-link to="/" class="btn-outline">取消</router-link>
        </div>
      </form>
    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '../../components/layout/Header.vue'
import Footer from '../../components/layout/Footer.vue'
import { useOrderStore } from '../../store/order'
import { useServiceStore } from '../../store/service'
import { usePetStore } from '../../store/pet'

const router = useRouter()
const orderStore = useOrderStore()
const serviceStore = useServiceStore()
const petStore = usePetStore()

// 服务类型列表
const serviceTypes = ref([])
// 宠物列表
const pets = ref([])
// 订单表单
const orderForm = ref({
  serviceType: '',
  serviceDate: '',
  serviceStartTime: '',
  serviceEndTime: '',
  petId: '',
  address: '',
  phone: '',
  notes: ''
})
// 加载状态
const isLoading = ref(false)

// 计算订单金额
const orderPrice = computed(() => {
  const selectedService = serviceTypes.value.find(s => s.id === orderForm.value.serviceType)
  if (!selectedService) return 0
  
  // 简单计算：服务时长（小时）* 单价
  const startTime = new Date(`2000-01-01 ${orderForm.value.serviceStartTime}`)
  const endTime = new Date(`2000-01-01 ${orderForm.value.serviceEndTime}`)
  const hours = (endTime - startTime) / (1000 * 60 * 60)
  
  return Math.round(selectedService.price * hours)
})

// 处理表单提交
    const handleSubmit = async () => {
      isLoading.value = true
      try {
        const orderData = {
          ...orderForm.value,
          serviceTime: `${orderForm.value.serviceDate} ${orderForm.value.serviceStartTime}-${orderForm.value.serviceEndTime}`,
          price: orderPrice.value
        }
        
        await orderStore.createOrder(orderData)
        // 跳转到订单列表
        router.push('/user/orders')
      } catch (error) {
        console.error('创建订单失败', error)
      } finally {
        isLoading.value = false
      }
    }

onMounted(async () => {
  try {
    // 这里应该从后端获取服务类型和宠物列表
    // 暂时使用模拟数据
    serviceTypes.value = [
      {
        id: 1,
        name: '宠物陪伴',
        price: 50
      },
      {
        id: 2,
        name: '宠物喂食',
        price: 30
      },
      {
        id: 3,
        name: '宠物遛弯',
        price: 40
      },
      {
        id: 4,
        name: '宠物清洁',
        price: 60
      }
    ]
    
    pets.value = [
      {
        id: 1,
        name: '小白',
        breed: '萨摩耶'
      },
      {
        id: 2,
        name: '小黑',
        breed: '英短'
      },
      {
        id: 3,
        name: '小橘',
        breed: '橘猫'
      }
    ]
  } catch (error) {
    console.error('获取数据失败', error)
  }
})
</script>

<style scoped>
.create-order {
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

.create-order-form {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--spacing-2xl);
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
}

.form-section {
  margin-bottom: var(--spacing-xl);
  padding-bottom: var(--spacing-lg);
  border-bottom: 1px solid var(--border-color);
}

.form-section:last-child {
  border-bottom: none;
  margin-bottom: var(--spacing-lg);
  padding-bottom: 0;
}

.form-section h3 {
  margin-bottom: var(--spacing-lg);
  color: var(--primary-color);
  font-size: 1.125rem;
}

.form-group {
  margin-bottom: var(--spacing-md);
  position: relative;
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

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--spacing-md);
}

.add-pet-link {
  position: absolute;
  right: 0;
  top: 0;
  color: var(--cta-color);
  font-size: 0.875rem;
  text-decoration: none;
  transition: color var(--transition-fast);
}

.add-pet-link:hover {
  color: var(--primary-color);
}

.order-price {
  background-color: var(--background-color);
  padding: var(--spacing-lg);
  border-radius: var(--border-radius-md);
}

.price-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: var(--spacing-sm);
}

.price-item.total {
  margin-top: var(--spacing-md);
  padding-top: var(--spacing-md);
  border-top: 1px solid var(--border-color);
  font-weight: 600;
}

.price-value.total {
  color: var(--cta-color);
  font-size: 1.25rem;
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
  
  .create-order-form {
    padding: var(--spacing-lg);
    margin: 0 var(--spacing-md) var(--spacing-xl);
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .add-pet-link {
    position: static;
    display: inline-block;
    margin-top: var(--spacing-xs);
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

import { defineStore } from 'pinia'
import axios from '../api/axios'

export const usePetStore = defineStore('pet', {
  state: () => ({
    pets: [],
    currentPet: null
  }),
  
  getters: {
    getPets: (state) => state.pets,
    getCurrentPet: (state) => state.currentPet
  },
  
  actions: {
    // 获取宠物列表
    async getPetList() {
      try {
        const response = await axios.get('/pet/list')
        // 后端返回的是Result对象，数据在data字段中
        if (response.data && response.data.code == 200) {
          this.pets = response.data.data
          return response.data.data
        } else {
          console.error('获取宠物列表失败:', response.data)
          throw new Error(response.data.message || '获取宠物列表失败')
        }
      } catch (error) {
        console.error('获取宠物列表失败:', error)
        throw error
      }
    },
    
    // 获取宠物详情（从本地列表中获取）
    getPetDetail(id) {
      const pet = this.pets.find(pet => pet.id === parseInt(id))
      this.currentPet = pet
      return pet
    },
    
    // 添加宠物
    async addPet(petData) {
      try {
        const response = await axios.post('/pet/add', petData)
        // 后端返回的是Result对象，数据在data字段中
        if (response.data && response.data.code === 200) {
          // 重新获取宠物列表
          await this.getPetList()
          return response.data
        } else {
          console.error('添加宠物失败:', response.data)
          throw new Error(response.data.message || '添加宠物失败')
        }
      } catch (error) {
        console.error('添加宠物失败:', error)
        throw error
      }
    },
    
    // 更新宠物信息
    async updatePet(id, petData) {
      try {
        // 构建更新请求，包含id
        const updateData = {
          id: parseInt(id),
          ...petData
        }
        const response = await axios.put('/pet/update', updateData)
        // 后端返回的是Result对象，数据在data字段中
        if (response.data && response.data.code === 200) {
          // 重新获取宠物列表
          await this.getPetList()
          return response.data
        } else {
          console.error('更新宠物失败:', response.data)
          throw new Error(response.data.message || '更新宠物失败')
        }
      } catch (error) {
        console.error('更新宠物失败:', error)
        throw error
      }
    },
    
    // 删除宠物
    async deletePet(id) {
      try {
        const response = await axios.delete(`/pet/${id}`)
        // 后端返回的是Result对象，数据在data字段中
        if (response.data && response.data.code === 200) {
          // 重新获取宠物列表
          await this.getPetList()
        } else {
          console.error('删除宠物失败:', response.data)
          throw new Error(response.data.message || '删除宠物失败')
        }
      } catch (error) {
        console.error('删除宠物失败:', error)
        throw error
      }
    }
  }
})

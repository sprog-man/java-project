import axios from './axios'

// 宠物API
export const petAPI = {
  // 获取宠物列表
  getPetList: () => {
    return axios.get('/pet')
  },
  
  // 获取宠物详情
  getPetDetail: (id) => {
    return axios.get(`/pet/${id}`)
  },
  
  // 添加宠物
  addPet: (petData) => {
    return axios.post('/pet', petData)
  },
  
  // 更新宠物信息
  updatePet: (id, petData) => {
    return axios.put(`/pet/${id}`, petData)
  },
  
  // 删除宠物
  deletePet: (id) => {
    return axios.delete(`/pet/${id}`)
  }
}

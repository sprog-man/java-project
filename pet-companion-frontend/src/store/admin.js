import { defineStore } from 'pinia'
import axios from '../api/axios'


//中心页面获取相关统计数据
export const useAdminStore = defineStore('admin', {
    state: () => ({
        stats: null, // 缓存统计数据
        userList: [], // 缓存用户列表
        totalUsers: 0, // 总用户数
        serviceList: [], // ✅ 新增：缓存服务类型列表
        petList: [], // ✅ 新增：缓存宠物列表
        totalPets: 0 // ✅ 新增：总宠物数
    }),
    actions: {
        // 获取后台仪表盘统计数据
        async fetchDashboardStats() {
            try {
                console.log('Admin Store: 开始获取统计数据')
                // ✅ 路径更新为 /admin/stats
                const response = await axios.get('/admin/stats')

                if (response.data && response.data.code === 200) {
                    this.stats = response.data.data
                    console.log('Admin Store: 统计数据更新成功', this.stats)
                    return this.stats
                } else {
                    throw new Error(response.data?.message || '获取统计数据失败')
                }
            } catch (error) {
                console.error('Admin Store: 获取统计数据异常', error)
                throw error
            }
        },
        // ✅ 新增：获取用户列表
        async fetchUserList(params = {}) {
            try {
                console.log('Admin Store: 开始获取用户列表', params)
                // ✅ 路径更新为 /admin/users，params 会自动转为 ?page=1&size=10...
                const response = await axios.get('/admin/users', { params })

                if (response.data && response.data.code === 200) {
                    const pageData = response.data.data
                    this.userList = pageData.records || []
                    this.totalUsers = pageData.total || 0
                    console.log('Admin Store: 用户列表更新成功', this.userList)
                    return pageData
                } else {
                    throw new Error(response.data?.message || '获取用户列表失败')
                }
            } catch (error) {
                console.error('Admin Store: 获取用户列表异常', error)
                throw error
            }
        },
        // ✅ 新增：管理员更新用户状态
        async updateUserStatus(userId, updates) {
            try {
                console.log(`Admin Store: 更新用户 ${userId} 的状态`, updates)
                // 对应后端: @PutMapping("/admin/users/{id}/status")
                const response = await axios.put(`/admin/users/${userId}/status`, updates)

                if (response.data && response.data.code === 200) {
                    console.log('Admin Store: 状态更新成功')
                    return true
                } else {
                    throw new Error(response.data?.message || '更新失败')
                }
            } catch (error) {
                console.error('Admin Store: 更新状态异常', error)
                throw error
            }
        },
        // ✅ 新增：管理员删除用户（逻辑删除）
        async deleteUser(userId) {
            try {
                console.log(`Admin Store: 删除用户 ${userId}`)
                // 对应后端: @DeleteMapping("/admin/users/{id}")
                const response = await axios.delete(`/admin/users/${userId}`)

                if (response.data && response.data.code === 200) {
                    console.log('Admin Store: 删除成功')
                    return true
                } else {
                    throw new Error(response.data?.message || '删除失败')
                }
            } catch (error) {
                console.error('Admin Store: 删除异常', error)
                throw error
            }
        },
        // ✅ 修改：获取服务类型列表 (路径改为 /admin/services)
        async fetchServiceList() {
            try {
                console.log('Admin Store: 开始获取服务类型列表')
                const response = await axios.get('/admin/services')

                if (response.data && response.data.code === 200) {
                    this.serviceList = response.data.data || []
                    console.log('Admin Store: 服务类型列表更新成功', this.serviceList)
                    return this.serviceList
                } else {
                    throw new Error(response.data?.message || '获取服务列表失败')
                }
            } catch (error) {
                console.error('Admin Store: 获取服务列表异常', error)
                throw error
            }
        },

        // ✅ 修改：添加服务类型
        async addService(serviceData) {
            try {
                const response = await axios.post('/admin/services', serviceData)
                // ✅ 兼容多种成功状态码 (200 或 0)
                if (response.data && (response.data.code === 200 || response.data.code === 0)) {
                    await this.fetchServiceList()
                    return true
                }
                throw new Error(response.data?.message || '添加失败')
            } catch (error) {
                console.error('Admin Store: 添加服务异常', error)
                throw error
            }
        },

        // ✅ 修改：更新服务类型
        async updateService(id, serviceData) {
            try {
                console.log(`正在更新服务 ID: ${id}`, serviceData);
                const response = await axios.put(`/admin/services/${id}`, serviceData);

                if (response.data && (response.data.code === 200 || response.data.code === 0)) {
                    await this.fetchServiceList();
                    return true;
                }
                throw new Error(response.data?.message || '更新失败');
            } catch (error) {
                console.error('Admin Store: 更新服务异常', error);
                // ✅ 打印出详细的错误响应
                if (error.response) {
                    console.error('后端返回的错误详情:', error.response.data);
                    throw new Error(error.response.data?.message || `服务器错误: ${error.response.status}`);
                }
                throw error;
            }
        },

        // ✅ 修改：删除服务类型
        async deleteService(id) {
            try {
                console.log(`正在删除服务 ID: ${id}`);
                const response = await axios.delete(`/admin/services/${id}`);

                if (response.data && (response.data.code === 200 || response.data.code === 0)) {
                    await this.fetchServiceList();
                    return true;
                }
                throw new Error(response.data?.message || '删除失败');
            } catch (error) {
                console.error('Admin Store: 删除服务异常', error);
                if (error.response) {
                    console.error('后端返回的错误详情:', error.response.data);
                    throw new Error(error.response.data?.message || `服务器错误: ${error.response.status}`);
                }
                throw error;
            }
        },
        // ✅ 新增：获取管理员端订单列表
        async fetchOrderList(params = {}) {
            try {
                console.log('Admin Store: 开始获取订单列表', params)
                const response = await axios.get('/admin/orders', { params })

                if (response.data && response.data.code === 200) {
                    // 返回的是分页对象 Page
                    return response.data.data
                } else {
                    throw new Error(response.data?.message || '获取订单列表失败')
                }
            } catch (error) {
                console.error('Admin Store: 获取订单列表异常', error)
                throw error
            }
        },
        // ✅ 新增：获取管理员端宠物列表
        async fetchPetList(params = {}) {
            try {
                console.log('Admin Store: 开始获取宠物列表', params)
                const response = await axios.get('/admin/pets', { params })

                if (response.data && response.data.code === 200) {
                    const pageData = response.data.data
                    this.petList = pageData.records || []
                    this.totalPets = pageData.total || 0
                    console.log('Admin Store: 宠物列表更新成功', this.petList)
                    return pageData
                } else {
                    throw new Error(response.data?.message || '获取宠物列表失败')
                }
            } catch (error) {
                console.error('Admin Store: 获取宠物列表异常', error)
                throw error
            }
        },
        // ✅ 新增：获取待审核的服务者列表
        async fetchPendingProviders() {
            try {
                const response = await axios.get('/admin/services/provider/pending')
                if (response.data && response.data.code === 200) {
                    return response.data.data || []
                }
                return []
            } catch (error) {
                console.error('Admin Store: 获取待审核列表异常', error)
                return []
            }
        },
        // ✅ 新增：执行审核
        async auditProvider(providerId, status) {
            try {
                const response = await axios.post(`/admin/services/provider/audit/${providerId}`, null, {
                    params: { status }
                })
                if (response.data && response.data.code === 200) {
                    return true
                }
                throw new Error(response.data?.message || '审核失败')
            } catch (error) {
                console.error('Admin Store: 审核异常', error)
                throw error
            }
        }
    }
})


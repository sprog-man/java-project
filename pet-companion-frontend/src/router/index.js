// 导入Vue Router的createRouter和createWebHistory函数
import { createRouter, createWebHistory } from 'vue-router'

// 创建路由实例
const router = createRouter({
  // 使用HTML5历史模式，去除URL中的#
  history: createWebHistory(),
  // 路由配置
  routes: [
    // 公共路由，不需要登录即可访问
    {
      path: '/', // 首页路径
      name: 'home', // 路由名称
      component: () => import('../views/home/Home.vue') // 动态导入首页组件
    },
    {
      path: '/login', // 登录页面路径
      name: 'login', // 路由名称
      component: () => import('../views/user/Login.vue') // 动态导入登录组件
    },
    {
      path: '/register', // 注册页面路径
      name: 'register', // 路由名称
      component: () => import('../views/user/Register.vue') // 动态导入注册组件
    },
    {
      path: '/service/type', // 服务类型列表路径
      name: 'serviceTypeList', // 路由名称
      component: () => import('../views/service/ServiceTypeList.vue') // 动态导入服务类型列表组件
    },
    {
      path: '/service/type/:id', // 服务类型详情路径，:id是动态参数
      name: 'serviceTypeDetail', // 路由名称
      component: () => import('../views/service/ServiceTypeDetail.vue') // 动态导入服务类型详情组件
    },
    
    // 管理员路由，需要管理员权限
    {
      path: '/admin/login', // 管理员登录路径
      name: 'adminLogin', // 路由名称
      component: () => import('../views/admin/AdminLogin.vue') // 动态导入管理员登录组件
    },
    {
      path: '/admin', // 管理员根路径
      component: () => import('../components/layout/AdminLayout.vue'), // 使用管理员布局组件
      meta: { requiresAuth: true, requiresAdmin: true }, // 路由元信息，需要登录和管理员权限
      children: [
        {
          path: 'center', // 管理员后台首页路径
          name: 'adminCenter', // 路由名称
          component: () => import('../views/admin/AdminCenter.vue'), // 动态导入管理员后台首页组件
        },
        {
          path: 'users', // 管理员用户管理路径
          name: 'adminUserList', // 路由名称
          component: () => import('../views/admin/UserList.vue'), // 动态导入用户管理组件
        },
        {
          path: 'services', // 管理员服务管理路径
          name: 'adminServiceList', // 路由名称
          component: () => import('../views/admin/ServiceList.vue'), // 动态导入服务管理组件
        },
        {
          path: 'orders', // 管理员订单管理路径
          name: 'adminOrderList', // 路由名称
          component: () => import('../views/admin/OrderList.vue'), // 动态导入订单管理组件
        },
        {
          path: 'pets', // 管理员宠物管理路径
          name: 'adminPetList', // 路由名称
          component: () => import('../views/admin/PetList.vue'), // 动态导入宠物管理组件
        },
        {
          path: 'reviews', // 管理员评价管理路径
          name: 'adminReviewList', // 路由名称
          component: () => import('../views/admin/ReviewList.vue'), // 动态导入评价管理组件
        },
        {
          path: 'audit', // 访问路径 /admin/audit
          name: 'AdminAudit',
          component: () => import('../views/admin/AuditList.vue') // 指向新文件
        }
      ]
    },

    {
      path: '/user/center', //用户中心
      name: 'userCenter',
      component: () => import('../views/user/UserCenter.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/user/edit', // 用户编辑信息路径
      name: 'editUserInfo',
      component: () => import('../views/user/EditUserInfo.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/user/change-password', // 修改密码路径
      name: 'changePassword',
      component: () => import('../views/user/ChangePassword.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/user/pets', // 用户宠物列表路径
      name: 'petList', // 路由名称
      component: () => import('../views/pet/PetList.vue'), // 动态导入宠物列表组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/user/pets/add', // 添加宠物路径
      name: 'addPet', // 路由名称
      component: () => import('../views/pet/AddPet.vue'), // 动态导入添加宠物组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/user/pets/:id', // 宠物详情路径，:id是动态参数
      name: 'petDetail', // 路由名称
      component: () => import('../views/pet/PetDetail.vue'), // 动态导入宠物详情组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/user/pets/:id/edit', // 编辑宠物路径，:id是动态参数
      name: 'editPet', // 路由名称
      component: () => import('../views/pet/EditPet.vue'), // 动态导入编辑宠物组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/user/orders', // 用户订单列表路径
      name: 'orderList', // 路由名称
      component: () => import('../views/order/OrderList.vue'), // 动态导入订单列表组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/user/orders/:id', // 用户订单详情路径，:id是动态参数
      name: 'orderDetail', // 路由名称
      component: () => import('../views/order/OrderDetail.vue'), // 动态导入订单详情组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/test/avatar-upload', // 头像上传测试路径
      name: 'avatarUpload', // 路由名称
      component: () => import('../views/test/AvatarUpload.vue'), // 动态导入头像上传测试组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/user/payments', // 用户支付记录路径
      name: 'paymentList', // 路由名称
      component: () => import('../views/payment/PaymentList.vue'), // 动态导入支付记录组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/user/reviews', // 用户评价列表路径
      name: 'userReviewList', // 路由名称
      component: () => import('../views/review/UserReviewList.vue'), // 动态导入用户评价列表组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    
    // 服务提供者路由，需要登录
    {
      path: '/server/dashboard', // 服务者仪表盘路径
      name: 'serverDashboard', // 路由名称
      component: () => import('../views/server/Dashboard.vue'), // 动态导入服务者仪表盘组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/server/order-hall', // 服务者订单大厅路径
      name: 'serverOrderHall', // 路由名称
      component: () => import('../views/server/OrderHall.vue'), // 动态导入服务者订单大厅组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/server/order-management', // 服务者订单管理路径
      name: 'serverOrderManagement', // 路由名称
      component: () => import('../views/server/OrderManagement.vue'), // 动态导入服务者订单管理组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/server/completed-orders', // 服务者已完成订单路径
      name: 'serverCompletedOrders', // 路由名称
      component: () => import('../views/server/CompletedOrders.vue'), // 动态导入服务者已完成订单组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/provider/apply', // 服务提供者申请路径
      name: 'providerApply', // 路由名称
      component: () => import('../views/service/ProviderApply.vue'), // 动态导入服务提供者申请组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/provider/center', // 服务提供者中心路径
      name: 'providerCenter', // 路由名称
      component: () => import('../views/service/ProviderCenter.vue'), // 动态导入服务提供者中心组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/provider/orders', // 服务提供者订单列表路径
      name: 'providerOrderList', // 路由名称
      component: () => import('../views/order/ProviderOrderList.vue'), // 动态导入服务提供者订单列表组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/provider/orders/pending', // 服务提供者待接单订单列表路径
      name: 'pendingOrderList', // 路由名称
      component: () => import('../views/order/PendingOrderList.vue'), // 动态导入待接单订单列表组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/provider/orders/:id', // 服务提供者订单详情路径，:id是动态参数
      name: 'providerOrderDetail', // 路由名称
      component: () => import('../views/order/ProviderOrderDetail.vue'), // 动态导入服务提供者订单详情组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/provider/reviews', // 服务提供者评价列表路径
      name: 'providerReviewList', // 路由名称
      component: () => import('../views/review/ProviderReviewList.vue'), // 动态导入服务提供者评价列表组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    
    // 订单路由，需要登录
    {
      path: '/order/create', // 创建订单路径
      name: 'createOrder', // 路由名称
      component: () => import('../views/order/CreateOrder.vue'), // 动态导入创建订单组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/order/:id', // 订单详情路径，:id是动态参数
      name: 'orderDetail', // 路由名称
      component: () => import('../views/order/OrderDetail.vue'), // 动态导入订单详情组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    
    // 评价路由
    {
      path: '/review/create/:orderId', // 创建评价路径，:orderId是动态参数
      name: 'createReview', // 路由名称
      component: () => import('../views/review/CreateReview.vue'), // 动态导入创建评价组件
      meta: { requiresAuth: true } // 路由元信息，需要登录
    },
    {
      path: '/review/:id', // 评价详情路径，:id是动态参数
      name: 'reviewDetail', // 路由名称
      component: () => import('../views/review/ReviewDetail.vue') // 动态导入评价详情组件
    }
  ]
})

// 路由守卫，用于权限控制
router.beforeEach((to, from, next) => {
  // 检查路由是否需要登录
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  // 检查路由是否需要管理员权限
  const requiresAdmin = to.matched.some(record => record.meta.requiresAdmin)
  // 从localStorage获取token
  const token = localStorage.getItem('token')
  // 从localStorage获取用户信息

  let userInfo = null;
  try {
    userInfo = JSON.parse(localStorage.getItem('userInfo'))
  } catch (e) {
    console.error("解析用户信息失败", e)
  }


  // 如果需要登录但没有token，跳转到登录页面
  if (requiresAuth && !token) {
    next('/login')
  }
  // ✅ 修复：如果 userInfo 为空或者 role 确实不是 1，才拦截
  else if (requiresAdmin && (!userInfo || userInfo.role !== 1)) {
    console.warn('非管理员尝试访问后台，已拦截')
    next('/login')
  }
  else {
    next()
  }
})

// 导出路由实例
export default router

# 同城上门宠物陪伴服务平台前端

## 项目简介

同城上门宠物陪伴服务平台是一个为用户提供专业宠物陪伴服务的前端应用。用户可以通过平台预约宠物陪伴、喂食、遛弯、清洁等服务，也可以申请成为服务提供者。

## 技术栈

- **框架**：Vue 3 + Vite
- **状态管理**：Pinia
- **路由**：Vue Router
- **HTTP客户端**：Axios
- **UI组件库**：Element Plus
- **样式预处理**：SCSS
- **构建工具**：Vite

## 项目结构

```
pet-companion-frontend/
├── public/                  # 静态资源
├── src/
│   ├── assets/             # 图片、图标等资源
│   ├── components/         # 通用组件
│   │   ├── common/         # 基础组件
│   │   ├── layout/         # 布局组件
│   │   └── business/       # 业务组件
│   ├── views/              # 页面视图
│   │   ├── user/           # 用户相关页面
│   │   ├── pet/            # 宠物相关页面
│   │   ├── service/        # 服务相关页面
│   │   ├── order/          # 订单相关页面
│   │   ├── payment/        # 支付相关页面
│   │   ├── review/         # 评价相关页面
│   │   └── home/           # 首页
│   ├── router/             # 路由配置
│   ├── store/              # Pinia状态管理
│   ├── api/                # API请求
│   ├── utils/              # 工具函数
│   ├── constants/          # 常量定义
│   ├── styles/             # 全局样式
│   ├── App.vue             # 根组件
│   └── main.js             # 入口文件
├── .env                    # 环境变量
├── index.html              # HTML模板
├── package.json            # 项目配置
└── vite.config.js          # Vite配置
```

## 快速开始

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## 功能模块

### 1. 用户模块
- 登录/注册
- 个人信息管理
- 服务提供者申请

### 2. 宠物模块
- 宠物信息管理
- 宠物列表
- 宠物详情

### 3. 服务模块
- 服务类型列表
- 服务类型详情
- 服务提供者管理

### 4. 订单模块
- 订单创建
- 订单状态管理
- 订单列表
- 订单详情

### 5. 支付模块
- 支付创建
- 支付记录

### 6. 评价模块
- 评价创建
- 评价回复
- 评价列表

## 设计系统

### 颜色
- 主色调：#0F172A（深蓝色）
- 辅助色：#334155（中蓝色）
- CTA色：#0369A1（亮蓝色）
- 背景色：#F8FAFC（浅灰色）
- 文字色：#020617（深黑色）

### 字体
- 标题：Varela Round
- 正文：Nunito Sans

### 间距
- 小：4px
- 中：8px
- 大：16px
- 超大：24px
- 特大：32px

### 边框半径
- 小：4px
- 中：8px
- 大：12px
- 超大：16px

## API 接口

前端通过以下 API 接口与后端进行交互：

### 1. 用户接口
- POST /api/user/register ：用户注册
- POST /api/user/login ：用户登录
- GET /api/user/info ：获取用户信息
- PUT /api/user/info ：更新用户信息

### 2. 宠物接口
- POST /api/pet ：添加宠物
- GET /api/pet ：获取宠物列表
- GET /api/pet/{id} ：获取宠物详情
- PUT /api/pet/{id} ：更新宠物信息
- DELETE /api/pet/{id} ：删除宠物

### 3. 服务接口
- GET /api/service/type ：获取服务类型列表
- GET /api/service/type/{id} ：获取服务类型详情
- POST /api/service/provider/apply ：申请成为服务提供者
- GET /api/service/provider/info ：获取服务提供者信息
- PUT /api/service/provider/info ：更新服务提供者信息

### 4. 订单接口
- POST /api/order ：创建订单
- GET /api/order ：获取订单列表
- GET /api/order/{id} ：获取订单详情
- PUT /api/order/{id}/cancel ：取消订单
- PUT /api/order/{id}/accept ：接受订单
- PUT /api/order/{id}/start ：开始服务
- PUT /api/order/{id}/complete ：完成服务
- GET /api/order/provider ：获取服务提供者订单列表
- GET /api/order/pending ：获取待接单订单列表

### 5. 支付接口
- POST /api/payment/create ：创建支付
- GET /api/payment/{id} ：获取支付记录
- GET /api/payment/order/{orderId} ：根据订单ID获取支付记录
- GET /api/payment/list ：获取用户支付记录列表
- POST /api/payment/callback ：支付回调

### 6. 评价接口
- POST /api/review/create ：创建评价
- GET /api/review/{id} ：获取评价详情
- GET /api/review/order/{orderId} ：根据订单ID获取评价
- POST /api/review/reply/{id} ：回复评价
- GET /api/review/user/list ：获取用户评价列表
- GET /api/review/provider/list ：获取服务提供者评价列表

## 浏览器兼容性

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## 许可证

MIT

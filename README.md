<h1> 项目大致说明 </h1>

## 一、项目需求

### 1. 核心需求

- 同城上门宠物陪伴服务平台，为用户提供专业的宠物陪伴服务
- 支持用户注册、登录、管理宠物信息
- 支持服务提供者申请、服务类型管理
- 支持订单创建、支付、状态管理
- 支持评价系统，方便用户和服务提供者互相评价
- 支持多线程并发处理，提高系统响应速度和吞吐量

### 2. 技术需求

- 使用 Spring Boot 3.4.4 作为基础框架
- 使用 MyBatis-Plus 3.5.7 作为 ORM 框架
- 使用 MySQL 8.0 作为数据库
- 使用 Spring Security 和 JWT 实现认证和授权
- 使用 Redis 实现缓存，提高系统性能
- 使用多线程并发处理，提高系统吞吐量

## 二、项目功能

### 1. 用户模块

- 用户注册 ：支持用户注册，包括手机号、密码等信息
- 用户登录 ：支持用户登录，返回 JWT 令牌
- 用户信息管理 ：支持用户查看和修改个人信息
- JWT 认证 ：基于 JWT 的无状态认证机制
- 权限控制 ：基于用户类型的权限控制

### 2. 宠物模块

- 宠物信息管理 ：支持用户添加、修改、删除、查询宠物信息
- 宠物列表 ：支持用户查看自己的宠物列表
- 宠物详情 ：支持查看宠物的详细信息

### 3. 服务模块

- 服务类型管理 ：支持查看服务类型列表和详情
- 服务提供者申请 ：支持用户申请成为服务提供者
- 服务提供者管理 ：支持服务提供者管理自己的信息
- 服务状态管理 ：支持服务状态的更新和查询

### 4. 订单模块

- 订单创建 ：支持用户创建订单，选择服务类型和时间
- 订单状态管理 ：支持订单状态的更新（待支付、待接单、服务中、待评价、已完成、已取消）
- 订单查询 ：支持用户和服务提供者查询订单列表
- 订单详情 ：支持查看订单的详细信息
- 分页查询 ：支持订单的分页查询

### 5. 支付模块

- 支付创建 ：支持用户创建支付，选择支付方式
- 支付回调 ：支持处理支付回调，更新支付状态
- 支付记录 ：支持查询支付记录

### 6. 评价模块

- 评价创建 ：支持用户对服务进行评价，包括评分和内容
- 评价回复 ：支持服务提供者对评价进行回复
- 评价查询 ：支持查询评价列表和详情

### 7. 系统模块

- 多线程并发处理 ：支持多线程处理用户请求，提高系统响应速度
- Redis 缓存 ：支持 Redis 缓存，提高系统性能
- 全局异常处理 ：支持全局异常处理，返回统一的错误响应
- 统一响应格式 ：支持统一的响应格式，包括状态码、消息和数据

## 三、项目结构

### 1. 目录结构

```
com.petcompany.platform/
├── PetCompanionApplication.java          # 项目启动类
├── config/                               # 配置类
│   ├── AsyncConfig.java                  # 异步任务配置
│   ├── SwaggerConfig.java                # Swagger 配置
│   └── WebMvcConfig.java                 # Web MVC 配置
├── common/                               # 公共类
│   ├── constant/                         # 常量
│   ├── exception/                        # 异常处理
│   ├── result/                           # 统一返回结果
│   ├── service/                          # 公共服务
│   └── utils/                            # 工具类
├── infrastructure/                       # 基础设施
│   └── security/                         # 安全配置
│       ├── annotation/                   # 注解
│       └── interceptor/                  # 拦截器
└── modules/                              # 业务模块
    ├── common/                           # 公共模块
    ├── order/                            # 订单模块
    ├── pet/                              # 宠物模块
    ├── payment/                          # 支付模块
    ├── review/                           # 评 价模块
    ├── service/                          # 服务模块
    └── user/                             # 用户模块
```

### 2. 模块职责

- config ：负责系统配置，包括异步任务、Swagger 和 Web MVC 配置
- common ：负责公共功能，包括常量、异常处理、统一返回结果和工具类
- infrastructure ：负责基础设施，包括安全配置和拦截器
- modules ：负责业务功能，按业务领域划分为多个模块

# 项目接口清单
## 一、用户模块
### 1. UserController
- POST /user/register ：用户注册
- POST /user/login ：用户登录
- POST /user/admin/login ：管理员登录
- POST /user/{id}/avatar ：上传头像
- PUT /user/center/modify ：更新用户信息
- GET /user/info ：获取用户信息
- PUT /user/center/password ：修改密码
### 2. AdminController
- GET /admin/users ：管理员获取用户列表（支持分页和筛选）
- GET /admin/stats ：管理员获取后台统计数据
- PUT /admin/users/{id}/status ：管理员更新用户状态
- DELETE /admin/users/{id} ：管理员删除用户（逻辑删除）
## 二、宠物模块
### 1. PetController
- POST /pet/add ：创建宠物
- PUT /pet/update ：更新宠物
- DELETE /pet/{id} ：删除宠物
- GET /pet/list ：获取宠物列表
### 2. AdminPetController
- GET /admin/pets ：管理员获取宠物列表（支持分页和筛选）
## 三、服务模块
### 1. ServiceTypeController
- GET /service-type/list ：获取所有服务类型
- GET /service-type/{id} ：根据ID获取服务类型
### 2. ServiceProviderController
- POST /service-provider/apply ：申请成为服务提供者
- GET /service-provider/info ：获取服务提供者信息
- GET /service-provider/pending-orders ：获取待接单的订单列表
- POST /service-provider/accept-order/{orderId} ：接受订单
- POST /service-provider/start-service/{orderId} ：开始服务
- POST /service-provider/complete-service/{orderId} ：完成服务
- GET /service-provider/orders ：获取服务提供者的订单列表
- GET /service-provider/stats/completed-orders ：统计已完成订单数
### 3. AdminServiceController
- GET /admin/services/provider/pending ：管理员获取待审核的服务者申请列表
- POST /admin/services/provider/audit/{providerId} ：管理员审核服务提供者申请
- GET /admin/services ：管理员获取所有服务类型列表
- POST /admin/services ：管理员添加服务类型
- PUT /admin/services/{id} ：管理员更新服务类型
- DELETE /admin/services/{id} ：管理员删除服务类型（逻辑删除）
## 四、订单模块
### 1. OrderController
- GET /order ：获取当前用户的订单列表
- GET /order/{id} ：获取订单详情
- POST /order ：创建订单
- PUT /order/{id}/cancel ：取消订单
- PUT /order/{id}/accept ：接受订单 (服务者端)
- PUT /order/{id}/start ：开始服务
- PUT /order/{id}/complete ：完成服务
- GET /order/provider ：获取服务提供者订单列表
- GET /order/pending ：获取待接单订单列表
- GET /order/provider/stats ：获取服务者订单统计数据 (Dashboard 专用)
### 2. AdminOrderController
- GET /admin/orders ：管理员获取订单列表（支持分页和筛选）
- GET /admin/orders/{id} ：管理员获取订单详情
- GET /admin/orders/status-options ：获取订单状态枚举列表（供前端筛选下拉框使用）
## 五、支付模块
### PaymentController
- POST /payment/create ：创建支付
- GET /payment/{id} ：获取支付记录
- GET /payment/order/{orderId} ：根据订单ID获取支付记录
- GET /payment/list ：获取用户支付记录列表
- POST /payment/callback ：支付回调
- GET /payment/mock/{orderId} ：模拟支付成功接口 (仅供开发测试使用)
## 六、评价模块
### ReviewController
- POST /review/create ：创建评价
- POST /review/reply/{id} ：回复评价
- GET /review/{id} ：获取评价详情
- GET /review/order/{orderId} ：根据订单ID获取评价
- GET /review/user/list ：获取用户评价列表
- GET /review/provider/list ：获取服务提供者评价列表
## 七、系统模块
- GET /api/health ：健康检查
- GET /api/test ：测试接口

## 五、技术实现亮点

### 1. 多线程并发处理

- 配置了线程池，核心线程数 10，最大线程数 50，队列容量 100
- 优化了 Tomcat 配置，最大线程数 200，最小线程数 20，最大连接数 10000
- 使用 @Async 注解实现异步任务，提高系统响应速度

### 2. 安全认证

- 使用 Spring Security 和 JWT 实现无状态认证
- 实现了基于用户类型的权限控制
- 配置了权限拦截器，确保接口安全

### 3. 性能优化

- 使用 Redis 缓存，提高系统性能
- 实现了数据库查询优化，包括分页查询
- 配置了连接池，提高数据库访问效率

### 4. 代码质量

- 严格遵循阿里巴巴 Java 开发规范
- 使用 Lombok 减少样板代码
- 实现了完整的异常处理和日志记录
- 提供了详细的 Javadoc 注释

## 六、项目部署

### 1. 环境要求

- JDK 17
- MySQL 8.0
- Redis（可选）

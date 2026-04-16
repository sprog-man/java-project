-- 宠伴服务平台数据库初始化脚本

-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像路径',
  `user_type` TINYINT DEFAULT 0 COMMENT '用户类型：0-宠物主，1-服务提供者，2-两者皆是',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-正常，1-禁用',
  `verified` TINYINT DEFAULT 0 COMMENT '认证状态：0-未认证，1-已认证',
    `role` TINYINT DEFAULT 0 COMMENT '角色：0-普通用户，1-管理员',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 宠物表
CREATE TABLE IF NOT EXISTS `pet` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '宠物ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `name` VARCHAR(50) NOT NULL COMMENT '宠物名称',
  `type` TINYINT NOT NULL COMMENT '宠物类型：1-猫，2-狗，3-其他',
  `breed` VARCHAR(50) DEFAULT NULL COMMENT '宠物品种',
  `age` INT DEFAULT NULL COMMENT '宠物年龄',
  `gender` TINYINT DEFAULT NULL COMMENT '宠物性别：1-公，2-母',
  `weight` DOUBLE DEFAULT NULL COMMENT '宠物体重',
  `photo` VARCHAR(255) DEFAULT NULL COMMENT '宠物照片',
  `description` TEXT DEFAULT NULL COMMENT '宠物描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物表';

-- 服务提供者表
CREATE TABLE IF NOT EXISTS `service_provider` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '提供者ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
     `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `id_card` VARCHAR(20) NOT NULL COMMENT '身份证号',
    `id_card_photo` VARCHAR(255) DEFAULT NULL COMMENT '身份证照片',
    `experience` TEXT DEFAULT NULL COMMENT '服务经验描述', -- ✅ 新增字段
    `verify_status` TINYINT DEFAULT 0 COMMENT '审核状态：0-待审核，1-审核通过，2-审核拒绝',
    `service_radius` INT DEFAULT 5 COMMENT '服务半径（公里）',
    `latitude` DOUBLE DEFAULT NULL COMMENT '纬度',
    `longitude` DOUBLE DEFAULT NULL COMMENT '经度',
    `rating` DOUBLE DEFAULT 5.0 COMMENT '平均评分',
    `review_count` INT DEFAULT 0 COMMENT '评价数量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务提供者表';

-- 服务类型表
CREATE TABLE IF NOT EXISTS `service_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '服务类型ID',
  `name` VARCHAR(50) NOT NULL COMMENT '服务名称',
  `description` TEXT DEFAULT NULL COMMENT '服务描述',
  `price` DOUBLE NOT NULL COMMENT '每小时价格',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务类型表';

-- 订单表
CREATE TABLE IF NOT EXISTS `pet_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `provider_id` BIGINT DEFAULT NULL COMMENT '服务提供者ID',
    `pet_id` BIGINT NOT NULL COMMENT '宠物ID',
    `service_type_id` BIGINT NOT NULL COMMENT '服务类型ID',
    `service_start_time` DATETIME NOT NULL COMMENT '服务开始时间',
    `service_end_time` DATETIME NOT NULL COMMENT '服务结束时间',
    `address` VARCHAR(255) NOT NULL COMMENT '服务地址',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `notes` TEXT DEFAULT NULL COMMENT '备注信息',
    `price` DOUBLE NOT NULL COMMENT '订单价格',
    `status` TINYINT DEFAULT 1 COMMENT '订单状态：1-待支付，2-待接单，3-已接单，4-服务中，5-已完成，6-已取消',
    `pay_status` TINYINT DEFAULT 0 COMMENT '支付状态：0-未支付，1-已支付，2-退款中，3-已退款',
    `review_status` TINYINT DEFAULT 0 COMMENT '评价状态：0-未评价，1-已评价',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_provider_id` (`provider_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ... existing code ...
-- 评价表
CREATE TABLE IF NOT EXISTS `review` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `user_id` BIGINT NOT NULL COMMENT '评价用户ID',
    `target_id` BIGINT NOT NULL COMMENT '被评价用户ID',
    `provider_id` BIGINT DEFAULT NULL COMMENT '服务提供者ID',
    `rating` TINYINT NOT NULL COMMENT '评分（1-5星）',
    `content` TEXT DEFAULT NULL COMMENT '评价内容',
    `images` TEXT DEFAULT NULL COMMENT '评价图片URL，多张用逗号分隔',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_target_id` (`target_id`),
    KEY `idx_provider_id` (`provider_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';
-- ... existing code ...

-- 支付记录表--
CREATE TABLE IF NOT EXISTS `payment_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '支付记录ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `amount` DOUBLE NOT NULL COMMENT '支付金额',
    `payment_method` VARCHAR(20) NOT NULL COMMENT '支付方式',
    `pay_order_no` VARCHAR(50) DEFAULT NULL COMMENT '内部支付单号',
    `transaction_id` VARCHAR(100) NOT NULL COMMENT '交易号',
    `status` TINYINT DEFAULT 0 COMMENT '支付状态：0-待支付，1-已支付，2-退款中，3-已退款',
    `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_transaction_id` (`transaction_id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';



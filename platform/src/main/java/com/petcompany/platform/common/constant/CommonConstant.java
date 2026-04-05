package com.petcompany.platform.common.constant;

/**
 * 公共常量类
 */
public class CommonConstant {

    /**
     * 成功状态码
     */
    public static final int SUCCESS = 200;

    /**
     * 失败状态码
     */
    public static final int FAIL = 500;

    /**
     * 未授权状态码
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 禁止访问状态码
     */
    public static final int FORBIDDEN = 403;

    /**
     * 资源不存在状态码
     */
    public static final int NOT_FOUND = 404;

    /**
     * 用户类型：宠物主
     */
    public static final int USER_TYPE_PET_OWNER = 0;

    /**
     * 用户类型：服务提供者
     */
    public static final int USER_TYPE_SERVICE_PROVIDER = 1;

    /**
     * 用户类型：两者皆是
     */
    public static final int USER_TYPE_BOTH = 2;

    /**
     * 订单状态：待支付
     */
    public static final int ORDER_STATUS_PENDING_PAYMENT = 1;

    /**
     * 订单状态：待接单
     */
    public static final int ORDER_STATUS_PENDING_ACCEPT = 2;

    /**
     * 订单状态：服务中
     */
    public static final int ORDER_STATUS_IN_SERVICE = 3;

    /**
     * 订单状态：待评价
     */
    public static final int ORDER_STATUS_PENDING_REVIEW = 4;

    /**
     * 订单状态：已完成
     */
    public static final int ORDER_STATUS_COMPLETED = 5;

    /**
     * 订单状态：已取消
     */
    public static final int ORDER_STATUS_CANCELLED = 6;

    /**
     * 支付状态：未支付
     */
    public static final int PAY_STATUS_UNPAID = 0;

    /**
     * 支付状态：已支付
     */
    public static final int PAY_STATUS_PAID = 1;

    /**
     * 支付状态：退款中
     */
    public static final int PAY_STATUS_REFUNDING = 2;

    /**
     * 支付状态：已退款
     */
    public static final int PAY_STATUS_REFUNDED = 3;

    /**
     * 审核状态：待审核
     */
    public static final int VERIFY_STATUS_PENDING = 0;

    /**
     * 审核状态：审核通过
     */
    public static final int VERIFY_STATUS_APPROVED = 1;

    /**
     * 审核状态：审核拒绝
     */
    public static final int VERIFY_STATUS_REJECTED = 2;

    /**
     * 宠物类型：猫
     */
    public static final int PET_TYPE_CAT = 1;

    /**
     * 宠物类型：狗
     */
    public static final int PET_TYPE_DOG = 2;

    /**
     * 宠物类型：其他
     */
    public static final int PET_TYPE_OTHER = 3;

    /**
     * 服务类型：上门喂养
     */
    public static final int SERVICE_TYPE_FEEDING = 1;

    /**
     * 服务类型：遛狗
     */
    public static final int SERVICE_TYPE_WALKING = 2;

    /**
     * 服务类型：带宠物出行
     */
    public static final int SERVICE_TYPE_TRIP = 3;

}

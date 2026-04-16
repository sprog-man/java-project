package com.petcompany.platform.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("sys_user")
public class User {

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 别名
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户类型：0-宠物主，1-服务提供者，2-两者皆是
     */
    private Integer userType;

    /**
     * 状态：0-正常，1-禁用
     */
    private Integer status;

    /**
     * 认证状态：0-未认证，1-已认证
     */
    private Integer verified;

    /**
     * 角色：0-普通用户，1-管理员
     */
    private Integer role;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     * 这个注解告诉 MyBatis-Plus：deleted 字段用于逻辑删除，而不是物理删除。
     */
    @TableLogic
    private Integer deleted;

}

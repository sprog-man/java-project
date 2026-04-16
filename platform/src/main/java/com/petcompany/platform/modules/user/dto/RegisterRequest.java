package com.petcompany.platform.modules.user.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 用户注册请求DTO
 */
@Data
public class RegisterRequest {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    /*
    * ^       1       [3-9]      \d{9}      $
│       │        │          │         │
│       │        │          │         └─ ⑤ 字符串结尾
│       │        │          └─────────── ④ 匹配9个数字
│       │        └────────────────────── ③ 第二位是3-9的数字
│       └─────────────────────────────── ② 第一位必须是1
└─────────────────────────────────────── ① 字符串开头

    * */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    /*^                          起始锚点
(?=.*[a-zA-Z])            ① 前瞻断言：必须包含至少一个字母
(?=.*\d)                  ② 前瞻断言：必须包含至少一个数字
[a-zA-Z\d@$!%*?&]{6,20}   ③ 字符集 + 量词：允许的字符和长度
$                         结束锚点
*/
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{6,20}$", message = "密码长度6-20位，包含字母和数字")
    private String password;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 昵称
     */
    private String nickname;

}

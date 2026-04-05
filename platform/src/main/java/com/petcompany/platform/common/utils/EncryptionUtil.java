package com.petcompany.platform.common.utils;

import cn.hutool.crypto.SecureUtil;

/**
 * 加密工具类
 */
public class EncryptionUtil {

    /**
     * MD5加密
     */
    public static String md5(String content) {
        return SecureUtil.md5(content);
    }

    /**
     * 加密身份证号
     */
    public static String encryptIdCard(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return idCard;
        }
        return idCard.substring(0, 3) + "***********" + idCard.substring(14);
    }

    /**
     * 加密手机号
     */
    public static String encryptPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

}

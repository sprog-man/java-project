package com.petcompany.platform;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class test {
    public static void main(String[] args) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = "admin123"; // 替换为你想要的密码
            String encodedPassword = encoder.encode(password);
            System.out.println("加密后的密码: " + encodedPassword);
    }

}

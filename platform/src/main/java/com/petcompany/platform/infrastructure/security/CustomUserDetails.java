package com.petcompany.platform.infrastructure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 自定义用户详情
 */
public class CustomUserDetails implements UserDetails {

    private Long userId;
    private String phone;
    private String password;
    private Integer userType;
    private List<GrantedAuthority> authorities;

    public CustomUserDetails(Long userId, String phone, String password, Integer userType, List<GrantedAuthority> authorities) {
        this.userId = userId;
        this.phone = phone;
        this.password = password;
        this.userType = userType;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getUserType() {
        return userType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

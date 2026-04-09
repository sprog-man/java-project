package com.petcompany.platform.infrastructure.security;

import com.petcompany.platform.modules.user.entity.User;
import com.petcompany.platform.modules.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户详情服务实现类
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户ID查询用户
        Long userId = Long.parseLong(username);
        User user = userMapper.selectById(userId);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 构建权限列表
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRole() == 1) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }

        // 构建UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                user.getPassword(),
                authorities
        );
    }

}

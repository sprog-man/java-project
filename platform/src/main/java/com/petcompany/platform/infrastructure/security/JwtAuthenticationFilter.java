package com.petcompany.platform.infrastructure.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.common.result.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.header}")
    private String header;

    @Resource
    private JwtService jwtService;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(header);
        
        log.debug("接收到的Authorization头: {}", authHeader);
        
        // 提取Token（去掉Bearer前缀）
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token != null && !token.isEmpty()) {
            try {
                // 验证令牌
                log.debug("开始解析Token: {}", token);
                var claims = jwtService.parseToken(token);
                Long userId = claims.get("userId").asLong();

                // 从数据库获取用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());

                // 设置认证信息
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JWTDecodeException e) {
                log.error("JWT解码失败: {}", e.getMessage());
                handleError(response, ResultCode.UNAUTHORIZED);
                return;
            } catch (JWTVerificationException e) {
                log.error("JWT验证失败: {}", e.getMessage());
                handleError(response, ResultCode.UNAUTHORIZED);
                return;
            } catch (Exception e) {
                log.error("认证失败: {}", e.getMessage());
                handleError(response, ResultCode.UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * 处理错误响应
     */
    private void handleError(HttpServletResponse response, ResultCode resultCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        Result<?> result = Result.fail(resultCode);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

}

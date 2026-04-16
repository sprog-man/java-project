package com.petcompany.platform.infrastructure.security.interceptor;

import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.infrastructure.security.UserContext;
import com.petcompany.platform.infrastructure.security.annotation.RequiresPermission;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 权限拦截器
 */
@Component
@Slf4j
public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取当前登录用户 ID
        Long userId = UserContext.getCurrentUserId();

        // 2. 如果不是登录用户，且接口需要登录，则拦截
        if (userId == null) {
            // 检查该接口是否需要登录（可以通过注解或路径判断）
            // 这里简单处理：如果 userId 为空，说明没登录
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"未登录\"}");
            return false;
        }

        // ✅ 3. 获取用户类型并增加空值保护
        Integer currentUserType = UserContext.getCurrentUserType();

        // 如果数据库里 user_type 是 null，我们暂时默认它为 0 (普通用户)
        if (currentUserType == null) {
            log.warn("用户 {} 的 user_type 为 null，默认按普通用户处理", userId);
            currentUserType = 0;
        }


        // 检查是否是方法处理器
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 检查是否有 @RequiresPermission 注解
            RequiresPermission annotation = handlerMethod.getMethodAnnotation(RequiresPermission.class);
            if (annotation != null) {
                // 获取允许的用户类型
                int[] userTypes = annotation.userTypes();

                // ❌ 删除这一行！不要再次从 UserContext 取值，否则会把上面的默认值覆盖掉
                // currentUserType = UserContext.getCurrentUserType();

                // 检查用户是否有权限
                boolean hasPermission = false;
                for (int userType : userTypes) {
                    // 现在这里的 currentUserType 保证不会是 null 了
                    if (userType == currentUserType) {
                        hasPermission = true;
                        break;
                    }
                }
                if (!hasPermission) {
                    throw new BusinessException("无权限访问");
                }
            }
        }
        return true;
    }

}

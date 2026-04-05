package com.petcompany.platform.infrastructure.security.interceptor;

import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.infrastructure.security.UserContext;
import com.petcompany.platform.infrastructure.security.annotation.RequiresPermission;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 权限拦截器
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查是否是方法处理器
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 检查是否有 @RequiresPermission 注解
            RequiresPermission annotation = handlerMethod.getMethodAnnotation(RequiresPermission.class);
            if (annotation != null) {
                // 获取允许的用户类型
                int[] userTypes = annotation.userTypes();
                // 获取当前用户类型
                Integer currentUserType = UserContext.getCurrentUserType();
                // 检查用户是否有权限
                boolean hasPermission = false;
                for (int userType : userTypes) {
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

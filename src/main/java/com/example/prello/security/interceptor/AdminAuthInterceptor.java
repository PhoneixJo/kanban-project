package com.example.prello.security.interceptor;

import com.example.prello.exception.CustomException;
import com.example.prello.exception.UserErrorCode;
import com.example.prello.user.enums.Auth;
import com.example.prello.common.SessionName;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        HttpSession session = request.getSession(false);

        Auth auth = (Auth) session.getAttribute(SessionName.USER_AUTH);

        if (auth != Auth.ADMIN) {
            throw new CustomException(UserErrorCode.FORBIDDEN_ADMIN);
        }

        return true;
    }
}
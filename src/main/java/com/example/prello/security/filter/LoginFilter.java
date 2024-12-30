package com.example.prello.security.filter;

import com.example.prello.common.SessionName;
import com.example.prello.exception.CustomException;
import com.example.prello.exception.ExceptionResponseDto;
import com.example.prello.exception.UserErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {
    private static final String[] WHITE_LIST = {"/api/signup", "/api/login"};
    private final ObjectMapper objetMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

       try {
           if (!PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI)) {
               HttpSession session = httpRequest.getSession(false);
               if (session == null || session.getAttribute(SessionName.USER_ID) == null) {
                   throw new CustomException(UserErrorCode.UNAUTHORIZED);
               }
           }
           chain.doFilter(request, response);
       } catch (CustomException e) {
           HttpServletResponse httpResponse = (HttpServletResponse) response;
           httpResponse.setStatus(e.getHttpStatus().value());
           httpResponse.setContentType("application/json;charset=UTF-8");

           ExceptionResponseDto errorResponse = new ExceptionResponseDto(e.getErrCode(), e.getMessage());
           httpResponse.getWriter().write(objetMapper.writeValueAsString(errorResponse));
       }
    }
}

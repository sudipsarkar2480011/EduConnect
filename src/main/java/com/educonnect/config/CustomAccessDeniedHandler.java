package com.educonnect.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        try {
            response.getWriter().write("{\"error\": \"Access Denied\", \"message\": \""
                    + accessDeniedException.getMessage() + "\", \"path\": \""
                    + request.getRequestURI() + "\"}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
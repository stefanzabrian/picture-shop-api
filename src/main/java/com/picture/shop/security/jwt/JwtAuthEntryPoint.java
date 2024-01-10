package com.picture.shop.security.jwt;

import com.picture.shop.controller.exception.JwtValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof JwtValidationException) {
            // Handle JwtValidationException
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        } else {
            // Handle other authentication exceptions
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
        }
    }
}

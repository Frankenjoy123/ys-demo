package com.yunsoo.api.rabbit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.common.error.ErrorResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/30
 * Descriptions:
 */
public class TokenInvalidAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        ErrorResult errorResult = new ErrorResult(40110, "Token invalid. " + authException.getMessage());
        mapper.writeValue(response.getOutputStream(), errorResult);
    }
}

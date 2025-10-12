package com.ainigma100.customerapi.security.handler;

import com.ainigma100.customerapi.dto.APIResponse;
import com.ainigma100.customerapi.dto.ErrorDTO;
import com.ainigma100.customerapi.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Collections;

/**
 * Custom handler for Authentication failures (401 Unauthorized) responses.
 * Returns a structured JSON response with error details.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        APIResponse<ErrorDTO> apiResponse = new APIResponse<>();
        apiResponse.setStatus(Status.FAILED.getValue());

        String message = (authException != null && authException.getMessage() != null)
                ? authException.getMessage()
                : "Authentication is required";

        // Log useful context for troubleshooting 401s
        String method = request.getMethod();
        String path = request.getRequestURI();
        String remote = request.getRemoteAddr();
        boolean hasAuthHeader = request.getHeader("Authorization") != null;
        log.warn("401 Unauthorized: method={} path={} remote={} hasAuthorizationHeader={} reason={}",
                method, path, remote, hasAuthHeader, message);

        apiResponse.setErrors(Collections.singletonList(new ErrorDTO("", message)));

        response.setHeader("luiss-auth-failure", "Authentication failed");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
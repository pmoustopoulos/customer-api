package com.ainigma100.customerapi.security.handler;

import com.ainigma100.customerapi.dto.APIResponse;
import com.ainigma100.customerapi.dto.ErrorDTO;
import com.ainigma100.customerapi.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.Collections;

/**
 * Custom handler for Access Denied (403 Forbidden) responses.
 * Returns a structured JSON response with error details.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        APIResponse<ErrorDTO> apiResponse = new APIResponse<>();
        apiResponse.setStatus(Status.FAILED.getValue());

        String errorMessage = (accessDeniedException != null && accessDeniedException.getMessage() != null)
                ? accessDeniedException.getMessage()
                : "Authorization failed";


        String method = request.getMethod();
        String path = request.getRequestURI();
        String remote = request.getRemoteAddr();
        String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";
        log.warn("403 Forbidden: method={} path={} remote={} user={} reason={}",
                method, path, remote, user, errorMessage);

        ErrorDTO errorDTO = new ErrorDTO("", errorMessage);
        apiResponse.setErrors(Collections.singletonList(errorDTO));

        response.setHeader("luiss-access-denied", "Access denied: insufficient permissions");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");

        // Write JSON response
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
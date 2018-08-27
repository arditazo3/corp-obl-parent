package com.tx.co.security.api.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tx.co.common.api.model.ApiErrorDetails;
import com.tx.co.security.exception.InvalidAuthenticationTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Entry point for JWT token-based authentication. Simply returns error details related to authentication failures.
 *
 * @author Ardit Azo
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    @Autowired
    public JwtAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    /**
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        HttpStatus status;
        ApiErrorDetails errorDetails = new ApiErrorDetails();

        if (authException instanceof InvalidAuthenticationTokenException) {
            status = HttpStatus.UNAUTHORIZED;
            errorDetails.setTitle(authException.getMessage());
            errorDetails.setMessage(authException.getCause().getMessage());
        } else {
            status = HttpStatus.FORBIDDEN;
            errorDetails.setTitle(status.getReasonPhrase());
            errorDetails.setMessage(authException.getMessage());
        }

        errorDetails.setStatus(status.value());
        errorDetails.setPath(request.getRequestURI());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mapper.writeValue(response.getWriter(), errorDetails);
    }
}
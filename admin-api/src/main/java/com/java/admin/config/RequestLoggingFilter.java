package com.java.admin.config;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
@Order(1)
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        try {
            MDC.put("requestPath", request.getRequestURI());

            String headersString = Collections.list(request.getHeaderNames())
                    .stream()
                    .map(headerName -> headerName + "=" + request.getHeader(headerName))
                    .collect(Collectors.joining(", "));

            MDC.put("requestHeaders", headersString);

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
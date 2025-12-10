package com.coopcredit.application.infrastructure.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import io.micrometer.core.instrument.MeterRegistry;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private final MeterRegistry meterRegistry;

    public AuthEntryPointJwt(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        meterRegistry.counter("security.auth.failures").increment();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}

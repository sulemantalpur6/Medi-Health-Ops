package com.hm.api.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteValidator {

    public static final List<String> OPEN_ENDPOINTS = List.of(
            "/api/auth/signup",
            "/api/auth/login",
            "/api/auth/refresh-token",
            "/actuator"
    );

    public boolean isOpenEndpoint(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        return OPEN_ENDPOINTS.stream()
                .anyMatch(path::startsWith);
    }
}
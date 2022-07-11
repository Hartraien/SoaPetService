package ru.hartraien.gatewayservice.services;

import org.springframework.http.server.reactive.ServerHttpRequest;

public interface RouteValidator {
    boolean isSecured(ServerHttpRequest request);

    boolean isForbidden(ServerHttpRequest request);
}

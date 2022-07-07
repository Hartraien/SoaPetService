package com.example.gatewayservice.services;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteValidatorImpl implements RouteValidator {

    private final List<String> unauthorizedPoints;
    private final List<String> securedPoints;

    public RouteValidatorImpl() {
        unauthorizedPoints = List.of("/login", "/register", "/validName");
        securedPoints = List.of("/pets");
    }


    @Override
    public boolean isSecured(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        return securedPoints.stream().noneMatch(path::startsWith);
    }

    @Override
    public boolean isForbidden(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        return !unauthorizedPoints.contains(path) && !isSecured(request);
    }
}

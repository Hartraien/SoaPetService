package com.example.gatewayservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteValidatorImpl implements RouteValidator {

    private final List<String> unauthorizedPoints;
    private final List<String> securedPoints;

    private final Logger logger = LoggerFactory.getLogger(RouteValidatorImpl.class);

    public RouteValidatorImpl() {
        unauthorizedPoints = List.of("/login", "/register", "/validName");
        securedPoints = List.of("/pets");
    }


    @Override
    public boolean isSecured(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        logger.info("isSecured: tried to access path = " + path);
        return securedPoints.stream().anyMatch(path::startsWith);
    }

    @Override
    public boolean isForbidden(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        logger.info("isForbidden: tried to access path = " + path);
        return !unauthorizedPoints.contains(path) && !isSecured(request);
    }
}

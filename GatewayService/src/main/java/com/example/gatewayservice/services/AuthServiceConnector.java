package com.example.gatewayservice.services;

import com.example.gatewayservice.DTOs.AuthServiceResponse;
import reactor.core.publisher.Mono;

public interface AuthServiceConnector {
    Mono<AuthServiceResponse> getInfoFromToken(String token);
}

package ru.hartraien.gatewayservice.services;

import reactor.core.publisher.Mono;
import ru.hartraien.gatewayservice.DTOs.AuthServiceResponse;

public interface AuthServiceConnector {
    Mono<AuthServiceResponse> getInfoFromToken(String token);
}

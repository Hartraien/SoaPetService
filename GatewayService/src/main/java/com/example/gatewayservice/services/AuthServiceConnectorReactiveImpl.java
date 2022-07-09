package com.example.gatewayservice.services;

import com.example.gatewayservice.DTOs.AuthServiceResponse;
import com.example.gatewayservice.exceptions.AuthServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceConnectorReactiveImpl implements AuthServiceConnector {

    private final WebClient webClient;
    private final String authServiceUrl;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(AuthServiceConnectorReactiveImpl.class);

    @Autowired
    public AuthServiceConnectorReactiveImpl(@Value("${services.auth-service-url}") String authServiceUrl
            , WebClient.Builder builder) {
        this.authServiceUrl = authServiceUrl;
        this.objectMapper = new ObjectMapper();

        this.webClient = builder
                .baseUrl(authServiceUrl)
                .build();
    }

    @Override
    public Mono<AuthServiceResponse> getInfoFromToken(String token) {
        logger.debug("Got into reactive getInfoFromToken");

        TokenRequest tokenRequest = new TokenRequest(token);
        AuthServiceResponse authServiceResponse;
        return this.webClient.post().uri("/verifyToken")
                .body(BodyInserters.fromValue(tokenRequest))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new AuthServiceException("Invalid Token")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new AuthServiceException("Auth service is not working")))
                .bodyToMono(AuthServiceResponse.class);
    }

    @FunctionalInterface
    public interface MethodToCall {
        void apply();
    }
}

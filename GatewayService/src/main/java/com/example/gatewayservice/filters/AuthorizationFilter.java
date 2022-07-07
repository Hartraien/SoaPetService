package com.example.gatewayservice.filters;

import com.example.gatewayservice.DTOs.AuthServiceResponse;
import com.example.gatewayservice.exceptions.AuthServiceException;
import com.example.gatewayservice.services.AuthServiceConnector;
import com.example.gatewayservice.services.RouteValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Hints;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@RefreshScope
@Component
public class AuthorizationFilter implements GatewayFilter {
    private final RouteValidator routeValidator;
    private final AuthServiceConnector authServiceConnector;

    @Value("${authorization.header-key}")
    private String authorizationKey;

    public AuthorizationFilter(RouteValidator routeValidator, AuthServiceConnector authServiceConnector) {
        this.routeValidator = routeValidator;
        this.authServiceConnector = authServiceConnector;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routeValidator.isForbidden(request))
            return this.onError(exchange, "No such service", HttpStatus.BAD_REQUEST);

        if (routeValidator.isSecured(request)) {
            if (this.authIsMissing(request))
                return this.onError(exchange, "Authorization header is missing", HttpStatus.UNAUTHORIZED);
            String token = this.getTokenFromRequest(request);
            try {
                AuthServiceResponse authServiceResponse = authServiceConnector.getInfoFromToken(token);
                this.modifyHeaders(exchange, authServiceResponse);
            } catch (AuthServiceException e) {
                return this.onError(exchange, "Authorization header is invalid login again using your username and password", HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    private void modifyHeaders(ServerWebExchange exchange, AuthServiceResponse authServiceResponse) {
        exchange.getRequest().mutate()
                .header("id", String.valueOf(authServiceResponse.getId()))
                .header("username", authServiceResponse.getUsername())
                .build();
    }

    private String getTokenFromRequest(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty(authorizationKey).get(0);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus statusCode) {
        final Map<String, String> error = Map.of("errorCode", String.valueOf(statusCode), "message", message);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(statusCode);
        return response.writeWith(new Jackson2JsonEncoder().encode(Mono.just(error),
                response.bufferFactory(),
                ResolvableType.forInstance(error),
                MediaType.APPLICATION_JSON,
                Hints.from(Hints.LOG_PREFIX_HINT, exchange.getLogPrefix())));
    }

    private boolean authIsMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(authorizationKey);
    }
}

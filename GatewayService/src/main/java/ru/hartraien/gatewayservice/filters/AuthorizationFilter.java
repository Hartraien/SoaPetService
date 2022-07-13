package ru.hartraien.gatewayservice.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
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
import ru.hartraien.gatewayservice.DTOs.AuthServiceResponse;
import ru.hartraien.gatewayservice.services.AuthServiceConnector;
import ru.hartraien.gatewayservice.services.RouteValidator;

import java.util.Map;

@RefreshScope
@Component
public class AuthorizationFilter implements GlobalFilter {
    private final RouteValidator routeValidator;
    private final AuthServiceConnector authServiceConnector;

    private final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    @Value("${authorization.header-key}")
    private String authorizationKey;

    public AuthorizationFilter(RouteValidator routeValidator, AuthServiceConnector authServiceConnector) {
        this.routeValidator = routeValidator;
        this.authServiceConnector = authServiceConnector;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        logger.debug("Entered filter");
        logger.debug("Checking if request is forbidden");
        if (routeValidator.isForbidden(request))
            return this.onError(exchange, "No such service", HttpStatus.BAD_REQUEST);

        logger.debug("Checking if request is secured");
        if (routeValidator.isSecured(request)) {
            if (this.authIsMissing(request)) {
                logger.debug("Authorization header is missing");
                return this.onError(exchange, "Authorization header is missing", HttpStatus.UNAUTHORIZED);
            }
            String token = this.getTokenFromRequest(request);
            logger.debug("Getting user info from token");
            Mono<AuthServiceResponse> infoFromToken = authServiceConnector.getInfoFromToken(token);
            logger.debug("Extracted user info from token");
            return infoFromToken
                    .flatMap(response -> {
                        logger.debug("Trying to modify headers");
                        modifyHeaders(exchange, response);
                        logger.debug("Successfully modified headers");
                        return chain.filter(exchange);
                    })
                    .onErrorResume(exception -> {
                        logger.debug("Exception message = " + exception.getMessage());
                        return this.onError(exchange, "Authorization header is invalid, login again using your username and password", HttpStatus.UNAUTHORIZED);
                    });
        }
        logger.debug("Propagating down the chain");
        return chain.filter(exchange);
    }

    private void modifyHeaders(ServerWebExchange exchange, AuthServiceResponse authServiceResponse) {
        logger.debug("Modifying headers");
        exchange.getRequest().mutate()
                .header("id", String.valueOf(authServiceResponse.getId()))
                .header("username", authServiceResponse.getUsername())
                .build();
    }

    private String getTokenFromRequest(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty(authorizationKey).get(0);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus statusCode) {
        logger.debug("Producing error mono for message " + message);
        final Map<String, String> error = Map.of("errorCode", String.valueOf(statusCode.value()), "message", message);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(statusCode);
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON.toString());
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

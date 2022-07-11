package ru.hartraien.gatewayservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class GatewayConfig {

    private final String petServiceUrl;
    private final String authServiceUrl;
    private final String userServiceUrl;

    public GatewayConfig(@Value("${services.pet-service-url}") String petServiceUrl
            , @Value("${services.auth-service-url}") String authServiceUrl
            , @Value("${services.user-service-url}") String userServiceUrl) {
        this.petServiceUrl = petServiceUrl;
        this.authServiceUrl = authServiceUrl;
        this.userServiceUrl = userServiceUrl;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/pets", "/pets/**", "/pet-types")
                        .uri(petServiceUrl))
                .route(p -> p
                        .path("/login", "/register")
                        .uri(authServiceUrl))
                .route(p -> p
                        .path("/validName")
                        .uri(userServiceUrl))
                .build();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }


    @RestController
    class ServiceInstanceRestController {

        @Autowired
        private DiscoveryClient discoveryClient;

        @RequestMapping("/service-instances/{applicationName}")
        public List<ServiceInstance> serviceInstancesByApplicationName(
                @PathVariable String applicationName) {
            return this.discoveryClient.getInstances(applicationName);
        }
    }
}

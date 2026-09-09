package com.service.salon.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service-route", r -> r
                        .path("/api/v1/auth/**")
                        .uri("http://auth-service:8082"))
                .route("catalog-service-route", r -> r
                        .path("/api/v1/catalog/**")
                        .uri("http://catalog-service:8081"))
                .route("booking-service-route", r -> r
                        .path("/api/v1/bookings/**", "/api/v1/master/schedule/**")
                        .uri("http://booking-service:8083"))
                .route("notification-service-route", r -> r
                        .path("/api/v1/notifications/**")
                        .uri("http://notification-service:8085"))
                .route("history-service-route", r -> r
                        .path("/api/v1/history/**")
                        .uri("http://history-service:8084"))
                .route("review-service-route", r -> r
                        .path("/api/v1/reviews/**")
                        .uri("http://review-service:8086"))
                .build();
    }
}

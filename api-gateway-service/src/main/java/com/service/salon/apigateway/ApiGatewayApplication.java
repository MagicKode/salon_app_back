package com.service.salon.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth", r -> r
                        .path("/auth/**")
                        .filters(f -> f.rewritePath("/auth/(?<segment>.*)", "/${segment}"))
                        .uri("http://auth-service:8082"))
                .route("catalog", r -> r
                        .path("/catalog/**")
                        .filters(f -> f.rewritePath("/catalog/(?<segment>.*)", "/${segment}"))
                        .uri("http://catalog-service:8081"))
                .route("booking", r -> r
                        .path("/booking/**")
                        .filters(f -> f.rewritePath("/booking/(?<segment>.*)", "/${segment}"))
                        .uri("http://booking-service:8083"))
                .route("notification", r -> r
                        .path("/notification/**")
                        .filters(f -> f.rewritePath("/notification/(?<segment>.*)", "/${segment}"))
                        .uri("http://notification-service:8085"))
                .route("history", r -> r
                        .path("/history/**")
                        .filters(f -> f.rewritePath("/history/(?<segment>.*)", "/${segment}"))
                        .uri("http://history-service:8084"))
                .route("review", r -> r
                        .path("/review/**")
                        .filters(f -> f.rewritePath("/review/(?<segment>.*)", "/${segment}"))
                        .uri("http://review-service:8086"))
                .build();
    }
}

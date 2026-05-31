package com.service.salon.apigateway.filter;

import com.service.salon.apigateway.util.JwtValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtValidator jwtValidator;

    private final List<String> openEndpoints = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login"
    );

    public AuthenticationFilter(JwtValidator jwtValidator) {
        super(Config.class);
        this.jwtValidator = jwtValidator;
    }

    public static class Config {
        // Класс конфигурации (пока пустой, так как настройки статические)
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // 1. Если эндпоинт открытый — пропускаем запрос дальше
            if (openEndpoints.stream().anyMatch(path::contains)) {
                return chain.filter(exchange);
            }

            // 2. Проверяем наличие заголовка Authorization
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                log.warn("Защищенный запрос отклонен: отсутствует заголовок Authorization для пути {}", path);
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Защищенный запрос отклонен: неверный формат заголовка для пути {}", path);
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            // Вырезаем сам токен из строки "Bearer eyJhbG..."
            String token = authHeader.substring(7);

            // 3. Валидируем токен
            if (jwtValidator.isTokenInvalid(token)) {
                log.warn("Защищенный запрос отклонен: токен невалиден или протух для пути {}", path);
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            // 4. Извлекаем username из токена
            String username = jwtValidator.getUsername(token); // Убедись, что этот метод есть в твой JwtValidator

            // 5. Мутируем запрос, добавляя заголовок для внутренних микросервисов
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Name", username)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    // Метод для красивого обрыва реактивного потока и возврата 401 ошибки
    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }
}

package com.service.salon.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter  implements WebFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Логируем входящий запрос (Pre-фильтрация)
        String path = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().toString();

        log.info("[Gateway] Входящий запрос: {} {}", method, path);

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    // Логируем ответ (Post-фильтрация)
                    log.info("[Gateway] Ответ для {} отправлен со статусом: {}",
                            path, exchange.getResponse().getStatusCode());
                }));
    }

    @Override
    public int getOrder() {
        // Высокий приоритет выполнения фильтра
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

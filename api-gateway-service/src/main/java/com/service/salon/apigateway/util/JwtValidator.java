package com.service.salon.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtValidator {
    private final SecretKey secretKey;

    public JwtValidator(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Проверяем токен "на вшивость"
    public boolean isTokenInvalid(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Если срок действия истек — токен невалидный
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            // Любая ошибка (битый токен, поддельная подпись) делает токен невалидным
            return true;
        }
    }

    public String getUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject(); // Достаем реальное имя пользователя из payload JWT
        } catch (Exception e) {
            return null;
        }
    }
}

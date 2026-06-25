package com.service.salon.auth.redisconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.service.salon.auth.model.UserEntity;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class AuthCacheConfig {

    private final ObjectMapper objectMapper;

    public AuthCacheConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy()
                .registerModule(new JavaTimeModule());
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer authCacheCustomizer() {
        Jackson2JsonRedisSerializer<UserEntity> userSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, UserEntity.class);

        return builder -> builder
                .withCacheConfiguration("userProfile",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(15))
                                .serializeKeysWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(userSerializer)));
    }
}

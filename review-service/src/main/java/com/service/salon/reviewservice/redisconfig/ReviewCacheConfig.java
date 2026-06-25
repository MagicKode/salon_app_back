package com.service.salon.reviewservice.redisconfig;

import com.service.salon.reviewservice.model.dto.ReviewStatsDto;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class ReviewCacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer reviewCacheCustomizer() {
        Jackson2JsonRedisSerializer<ReviewStatsDto> reviewSerializer = new Jackson2JsonRedisSerializer<>(ReviewStatsDto.class);

        return builder -> builder
                .withCacheConfiguration("reviews",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(5))
                                .serializeKeysWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(reviewSerializer)));
    }
}

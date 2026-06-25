package com.service.salon.commonservice.redisconfig;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()));
    }

//    @Bean
//    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//        return builder -> builder
//                .withCacheConfiguration("salon",
//                        RedisCacheConfiguration.defaultCacheConfig()
//                                .entryTtl(Duration.ofHours(1))
//                                .serializeValuesWith(RedisSerializationContext.SerializationPair
//                                        .fromSerializer(new GenericJackson2JsonRedisSerializer())))
//
//                .withCacheConfiguration("slots",
//                        RedisCacheConfiguration.defaultCacheConfig()
//                                .entryTtl(Duration.ofMinutes(2))
//                                .serializeValuesWith(RedisSerializationContext.SerializationPair
//                                        .fromSerializer(new GenericJackson2JsonRedisSerializer())))
//
//                .withCacheConfiguration("reviews",
//                        RedisCacheConfiguration.defaultCacheConfig()
//                                .entryTtl(Duration.ofMinutes(5))
//                                .serializeValuesWith(RedisSerializationContext.SerializationPair
//                                        .fromSerializer(new GenericJackson2JsonRedisSerializer())));
//    }

    @Bean
    public CacheErrorHandler errorHandler() {
        return new RedisErrorConfig();
    }
}

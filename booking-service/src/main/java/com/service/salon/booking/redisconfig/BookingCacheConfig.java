package com.service.salon.booking.redisconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.dto.DailyScheduleDto;
import com.service.salon.booking.model.dto.DayStatDto;
import com.service.salon.booking.model.dto.TimeSlotDto;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;

@Configuration
public class BookingCacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer bookingCacheCustomizer(ObjectMapper objectMapper) {

        // Сериализатор для List<TimeSlotDto> (слоты)
        Jackson2JsonRedisSerializer<List<TimeSlotDto>> slotsSerializer = new Jackson2JsonRedisSerializer<>(objectMapper,
                objectMapper.getTypeFactory().constructCollectionType(List.class, TimeSlotDto.class));

        // Сериализатор для DailyScheduleDto (расписание на сегодня)
        Jackson2JsonRedisSerializer<DailyScheduleDto> dailyScheduleSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, DailyScheduleDto.class);

        // Сериализатор для List<DayStatDto> (статистика месяца)
        Jackson2JsonRedisSerializer<List<DayStatDto>> statsSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, DayStatDto.class));

        // Сериализатор для List<Booking> (записи месяца)
        Jackson2JsonRedisSerializer<List<Booking>> appointmentsSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Booking.class));

        return builder -> builder
                .withCacheConfiguration("slots",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(2))
                                .serializeKeysWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(slotsSerializer)))

                .withCacheConfiguration("masterSchedule",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(2))
                                .serializeKeysWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(dailyScheduleSerializer)))

                .withCacheConfiguration("monthStats",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(5))
                                .serializeKeysWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(statsSerializer)))

                .withCacheConfiguration("monthAppointments",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(5))
                                .serializeKeysWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(appointmentsSerializer)));
    }
}

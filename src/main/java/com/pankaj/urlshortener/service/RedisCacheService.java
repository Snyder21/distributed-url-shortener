package com.pankaj.urlshortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisCacheService implements CacheService {

    private static final String PREFIX = "url:";

    private final StringRedisTemplate redisTemplate;

    @Override
    public Optional<String> get(String shortCode) {

        String value = redisTemplate.opsForValue()
                .get(PREFIX + shortCode);

        return Optional.ofNullable(value);
    }

    @Override
    public void put(String shortCode,
                    String longUrl) {

        redisTemplate.opsForValue()
                .set(
                        PREFIX + shortCode,
                        longUrl,
                        Duration.ofHours(24)
                );
    }
}

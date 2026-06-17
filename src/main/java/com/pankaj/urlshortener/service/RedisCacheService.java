package com.pankaj.urlshortener.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheService implements CacheService {

    private static final String PREFIX = "url:";

    private final StringRedisTemplate redisTemplate;

    @Override
    public Optional<String> get(String shortCode) {

        try {

            String value = redisTemplate.opsForValue()
                    .get(PREFIX + shortCode);

            return Optional.ofNullable(value);

        } catch (Exception ex) {

            log.error(
                    "Redis GET failed for key={}",
                    shortCode,
                    ex
            );

            return Optional.empty();
        }
    }

    @Override
    public void put(String shortCode, String longUrl) {

        try {

            redisTemplate.opsForValue()
                    .set(
                            PREFIX + shortCode,
                            longUrl,
                            Duration.ofHours(24)
                    );

        } catch (Exception ex) {

            log.error(
                    "Redis PUT failed for key={}",
                    shortCode,
                    ex
            );
        }
    }
}
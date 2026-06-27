package com.pankaj.urlshortener.ratelimiter;

import com.pankaj.urlshortener.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisRateLimiter implements RateLimiter {

    private static final String PREFIX = "rate_limit:create:";

    private final StringRedisTemplate redisTemplate;

    @Value("${app.rate-limit.create-url.max-requests}")
    private long maxRequests;

    @Value("${app.rate-limit.create-url.duration-seconds}")
    private long durationSeconds;

    @Override
    public void validateRequest(String clientId) {

        String key = PREFIX + clientId;

        Long currentCount = redisTemplate.opsForValue().increment(key);

        if (currentCount == null) {
            throw new IllegalStateException("Unable to increment Redis counter.");
        }

        if (currentCount == 1) {
            redisTemplate.expire(
                    key,
                    Duration.ofSeconds(durationSeconds)
            );
        }

        if (currentCount > maxRequests) {
            throw new RateLimitExceededException();
        }
    }
}
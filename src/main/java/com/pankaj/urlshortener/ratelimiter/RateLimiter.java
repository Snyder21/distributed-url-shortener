package com.pankaj.urlshortener.ratelimiter;

public interface RateLimiter {

    void validateRequest(String clientId);

}

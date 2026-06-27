package com.pankaj.urlshortener.exception;

public class RateLimitExceededException extends RuntimeException{
    public RateLimitExceededException() {
        super("Rate limit exceeded. Please try again later.");
    }
}

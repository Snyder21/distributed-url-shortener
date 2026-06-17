package com.pankaj.urlshortener.service;

import java.util.Optional;

public interface CacheService {

    Optional<String> get(String shortCode);

    void put(String shortCode,
             String longUrl);

}

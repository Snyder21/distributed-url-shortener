package com.pankaj.urlshortener.service;

import com.pankaj.urlshortener.dto.CreateUrlRequest;
import com.pankaj.urlshortener.dto.CreateUrlResponse;

public interface UrlService {
    CreateUrlResponse createShortUrl(CreateUrlRequest request);
    String getLongUrl(String shortCode);
}

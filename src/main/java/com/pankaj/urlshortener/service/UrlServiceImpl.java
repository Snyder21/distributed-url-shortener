package com.pankaj.urlshortener.service;

import com.pankaj.urlshortener.dto.CreateUrlRequest;
import com.pankaj.urlshortener.dto.CreateUrlResponse;
import com.pankaj.urlshortener.entity.UrlMapping;
import com.pankaj.urlshortener.exception.UrlNotFoundException;
import com.pankaj.urlshortener.repository.UrlRepository;
import com.pankaj.urlshortener.util.Base62Encoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final Base62Encoder base62Encoder;
    private final CacheService cacheService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    @Transactional
    public CreateUrlResponse createShortUrl(CreateUrlRequest request) {

        validateUrl(request.getLongUrl());

        UrlMapping mapping = UrlMapping.builder()
                .longUrl(request.getLongUrl())
                .build();

        mapping = urlRepository.save(mapping);

        String shortCode = base62Encoder.encode(mapping.getId());

        mapping.setShortCode(shortCode);

        try {
            cacheService.put(
                    shortCode,
                    mapping.getLongUrl()
            );
        } catch (Exception ex) {
            log.error(
                    "Failed to cache shortCode={}",
                    shortCode,
                    ex
            );
        }

        log.info(
                "Created short url. shortCode={}, longUrl={}",
                shortCode,
                mapping.getLongUrl()
        );

        return new CreateUrlResponse(
                baseUrl + "/" + shortCode
        );
    }

    @Override
    @Transactional(readOnly = true)
    public String getLongUrl(String shortCode) {

        try {

            Optional<String> cached =
                    cacheService.get(shortCode);

            if (cached.isPresent()) {

                log.info(
                        "Cache HIT for shortCode={}",
                        shortCode
                );

                return cached.get();
            }

            log.info(
                    "Cache MISS for shortCode={}",
                    shortCode
            );

        } catch (Exception ex) {

            log.error(
                    "Redis unavailable for shortCode={}",
                    shortCode,
                    ex
            );
        }

        UrlMapping mapping =
                urlRepository.findByShortCode(shortCode)
                        .orElseThrow(
                                () -> new UrlNotFoundException(shortCode)
                        );

        try {

            cacheService.put(
                    shortCode,
                    mapping.getLongUrl()
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to cache shortCode={}",
                    shortCode,
                    ex
            );
        }

        return mapping.getLongUrl();
    }

    private void validateUrl(String url) {

        try {

            URI uri = new URI(url);

            if (uri.getScheme() == null ||
                    (!uri.getScheme().equalsIgnoreCase("http")
                            && !uri.getScheme().equalsIgnoreCase("https"))) {

                throw new IllegalArgumentException(
                        "URL must start with http:// or https://"
                );
            }

        } catch (URISyntaxException ex) {

            throw new IllegalArgumentException(
                    "Invalid URL format"
            );
        }
    }
}
package com.pankaj.urlshortener.service;

import com.pankaj.urlshortener.dto.CreateUrlRequest;
import com.pankaj.urlshortener.dto.CreateUrlResponse;
import com.pankaj.urlshortener.entity.UrlMapping;
import com.pankaj.urlshortener.exception.UrlNotFoundException;
import com.pankaj.urlshortener.repository.UrlRepository;
import com.pankaj.urlshortener.util.Base62Encoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

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

        UrlMapping mapping = UrlMapping.builder()
                .longUrl(request.getLongUrl())
                .build();

        /*
         * First save:
         * MySQL generates ID
         */
        mapping = urlRepository.save(mapping);

        /*
         * Generate short code
         */
        String shortCode = base62Encoder.encode(mapping.getId());

        mapping.setShortCode(shortCode);
        cacheService.put(
                shortCode,
                mapping.getLongUrl()
        );
        /*
         * Second save:
         * Persist short code
         */
        urlRepository.save(mapping);

        return new CreateUrlResponse(
                baseUrl + "/" + shortCode
        );
    }

    @Override
    @Transactional(readOnly = true)
    public String getLongUrl(String shortCode) {

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

        UrlMapping mapping =
                urlRepository.findByShortCode(shortCode)
                        .orElseThrow(
                                () -> new UrlNotFoundException(shortCode)
                        );

        cacheService.put(
                shortCode,
                mapping.getLongUrl()
        );

        return mapping.getLongUrl();
    }
}
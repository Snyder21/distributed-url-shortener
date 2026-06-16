package com.pankaj.urlshortener.service;

import com.pankaj.urlshortener.dto.CreateUrlRequest;
import com.pankaj.urlshortener.dto.CreateUrlResponse;
import com.pankaj.urlshortener.entity.UrlMapping;
import com.pankaj.urlshortener.repository.UrlRepository;
import com.pankaj.urlshortener.util.Base62Encoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final Base62Encoder base62Encoder;

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

        /*
         * Second save:
         * Persist short code
         */
        urlRepository.save(mapping);

        return new CreateUrlResponse(
                baseUrl + "/" + shortCode
        );
    }
}
package com.pankaj.urlshortener.controller;

import com.pankaj.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlRedirectController {

    private final UrlService urlService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode) {

        String longUrl =
                urlService.getLongUrl(shortCode);

        HttpHeaders headers =
                new HttpHeaders();

        headers.setLocation(
                URI.create(longUrl)
        );

        return new ResponseEntity<>(
                headers,
                HttpStatus.FOUND
        );
    }
}

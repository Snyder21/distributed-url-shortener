package com.pankaj.urlshortener.controller;

import com.pankaj.urlshortener.dto.CreateUrlRequest;
import com.pankaj.urlshortener.dto.CreateUrlResponse;
import com.pankaj.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<CreateUrlResponse> createShortUrl(
            @Valid @RequestBody CreateUrlRequest request) {

        CreateUrlResponse response =
                urlService.createShortUrl(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}
package com.pankaj.urlshortener.controller;

import com.pankaj.urlshortener.dto.AnalyticsResponse;
import com.pankaj.urlshortener.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/{shortCode}")
    public AnalyticsResponse getAnalytics(
            @PathVariable String shortCode) {

        return analyticsService
                .getAnalytics(shortCode);
    }
}

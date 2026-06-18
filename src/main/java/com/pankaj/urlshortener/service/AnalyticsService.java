package com.pankaj.urlshortener.service;

import com.pankaj.urlshortener.dto.AnalyticsResponse;

public interface AnalyticsService {

    void recordClick(
            String shortCode,
            String ipAddress,
            String userAgent);

    AnalyticsResponse getAnalytics(
            String shortCode);
}

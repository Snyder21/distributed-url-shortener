package com.pankaj.urlshortener.service;

import com.pankaj.urlshortener.dto.AnalyticsResponse;
import com.pankaj.urlshortener.entity.ClickEvent;
import com.pankaj.urlshortener.repository.ClickEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl
        implements AnalyticsService {

    private final ClickEventRepository clickEventRepository;

    @Override
    public void recordClick(String shortCode, String ipAddress, String userAgent) {


        try {
            ClickEvent event =
                    ClickEvent.builder()
                            .shortCode(shortCode)
                            .ipAddress(ipAddress)
                            .userAgent(userAgent)
                            .build();

            clickEventRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public AnalyticsResponse getAnalytics(String shortCode) {

        try {
            long clicks = clickEventRepository.countByShortCode(shortCode);

            return AnalyticsResponse.builder()
                    .shortCode(shortCode)
                    .clicks(clicks)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

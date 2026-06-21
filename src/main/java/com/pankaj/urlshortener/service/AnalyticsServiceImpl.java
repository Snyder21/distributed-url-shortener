package com.pankaj.urlshortener.service;

import com.pankaj.urlshortener.dto.AnalyticsResponse;
import com.pankaj.urlshortener.entity.ClickEvent;
import com.pankaj.urlshortener.entity.UrlStats;
import com.pankaj.urlshortener.repository.ClickEventRepository;
import com.pankaj.urlshortener.repository.UrlStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ClickEventRepository clickEventRepository;
    private final UrlStatsRepository urlStatsRepository;

    @Override
    public void recordClick(String shortCode, String ipAddress, String userAgent) {

        log.info("Recording click for shortCode={}, ip={}",
                shortCode, ipAddress);

        try {
            ClickEvent event = ClickEvent.builder()
                    .shortCode(shortCode)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .build();

            clickEventRepository.save(event);

            UrlStats stats = urlStatsRepository
                    .findById(shortCode)
                    .orElse(new UrlStats(shortCode, 0L));

            stats.setClickCount(stats.getClickCount() + 1);

            urlStatsRepository.save(stats);

            log.info("Successfully recorded click for shortCode={}, totalClicks={}",
                    shortCode, stats.getClickCount());

        } catch (DataAccessException e) {
            log.error("Database error while recording click for shortCode={}",
                    shortCode, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while recording click for shortCode={}",
                    shortCode, e);
            throw new IllegalStateException(
                    "Failed to record click for shortCode: " + shortCode, e);
        }
    }

    @Override
    public AnalyticsResponse getAnalytics(String shortCode) {

        log.info("Fetching analytics for shortCode={}", shortCode);

        try {
            UrlStats stats = urlStatsRepository.findById(shortCode)
                    .orElseThrow(() -> {
                        log.warn("Analytics not found for shortCode={}", shortCode);
                        return new NoSuchElementException(
                                "Analytics not found for shortCode: " + shortCode);
                    });

            log.info("Analytics fetched successfully for shortCode={}, clicks={}",
                    shortCode, stats.getClickCount());

            return AnalyticsResponse.builder()
                    .shortCode(shortCode)
                    .clicks(stats.getClickCount())
                    .build();

        } catch (NoSuchElementException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while fetching analytics for shortCode={}",
                    shortCode, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching analytics for shortCode={}",
                    shortCode, e);
            throw new IllegalStateException(
                    "Failed to fetch analytics for shortCode: " + shortCode, e);
        }
    }
}
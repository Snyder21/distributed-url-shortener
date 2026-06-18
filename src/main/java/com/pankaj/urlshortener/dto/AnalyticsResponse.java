package com.pankaj.urlshortener.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyticsResponse {

    private String shortCode;

    private Long clicks;
}

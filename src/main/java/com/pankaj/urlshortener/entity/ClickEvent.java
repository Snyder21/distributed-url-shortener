package com.pankaj.urlshortener.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "click_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClickEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shortCode;

    private LocalDateTime clickedAt;

    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String userAgent;

    @PrePersist
    public void prePersist() {
        clickedAt = LocalDateTime.now();
    }
}

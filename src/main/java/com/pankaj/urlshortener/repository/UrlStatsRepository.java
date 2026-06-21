package com.pankaj.urlshortener.repository;

import com.pankaj.urlshortener.entity.UrlStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlStatsRepository extends JpaRepository<UrlStats,String> {
    Optional<UrlStats> findById(String shortCode);
}

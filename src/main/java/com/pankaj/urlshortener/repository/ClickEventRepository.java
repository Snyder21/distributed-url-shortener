package com.pankaj.urlshortener.repository;

import com.pankaj.urlshortener.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickEventRepository
        extends JpaRepository<ClickEvent, Long> {

    long countByShortCode(String shortCode);

}

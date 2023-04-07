package com.url.urlshortener.repository;

import com.url.urlshortener.entity.UrlMap;
import com.url.urlshortener.entity.VisitHistory;
import com.url.urlshortener.entity.VisitHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitHistoryRepository extends JpaRepository<VisitHistory, VisitHistoryId> {
    Long countById_UrlMap(UrlMap urlMap);
}

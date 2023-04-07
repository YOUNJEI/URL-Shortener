package com.url.urlshortener.repository;

import com.url.urlshortener.controller.dto.UrlListResponseInterface;
import com.url.urlshortener.entity.UrlMap;
import com.url.urlshortener.entity.UrlMapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlMapRepository extends JpaRepository<UrlMap, UrlMapId> {
    boolean existsByShortUrl(String shortUrl);
    Optional<UrlMap> findByShortUrl(String shortUrl);

    @Query("SELECT DISTINCT u.id.origin AS origin, u.shortUrl AS shortUrl, u.created AS created, COUNT(v) AS visitors " +
            "FROM UrlMap u LEFT JOIN VisitHistory v ON u.shortUrl = v.id.urlMap " +
            "WHERE u.id.owner = :owner " +
            "GROUP BY u.shortUrl")
    List<UrlListResponseInterface> findDistinctUrlMapList(String owner);
}

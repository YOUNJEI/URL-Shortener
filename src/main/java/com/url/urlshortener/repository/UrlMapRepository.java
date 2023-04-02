package com.url.urlshortener.repository;

import com.url.urlshortener.entity.UrlMap;
import com.url.urlshortener.entity.UrlMapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMapRepository extends JpaRepository<UrlMap, UrlMapId> {
    boolean existsByShortUrl(String shortUrl);
    Optional<UrlMap> findByShortUrl(String shortUrl);
}

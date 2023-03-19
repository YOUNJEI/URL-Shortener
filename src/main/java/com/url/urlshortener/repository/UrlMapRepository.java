package com.url.urlshortener.repository;

import com.url.urlshortener.entity.UrlMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMapRepository extends JpaRepository<UrlMap, String> {
    boolean existsByShortUrl(String shortUrl);
    Optional<UrlMap> findByShortUrl(String shortUrl);
}

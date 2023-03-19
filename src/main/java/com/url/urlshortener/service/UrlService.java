package com.url.urlshortener.service;

import com.url.urlshortener.controller.dto.UrlCreateRequestDto;
import com.url.urlshortener.entity.UrlMap;
import com.url.urlshortener.exception.PageNotFoundException;
import com.url.urlshortener.repository.UrlMapRepository;
import com.url.urlshortener.utility.ShortenerAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UrlService {
    private final UrlMapRepository urlMapRepository;
    private final ShortenerAlgorithm shortenerAlgorithm;

    public ResponseEntity<String> createUrl(UrlCreateRequestDto urlCreateRequestDto) {
        Optional<UrlMap> urlMap = urlMapRepository.findById(urlCreateRequestDto.getOrigin());

        if (urlMap.isPresent())
            return ResponseEntity.ok().body(urlMap.get().getShortUrl());

        // create unique short URL
        String shortUrl = urlCreateRequestDto.getOrigin();
        do {
            shortUrl = shortenerAlgorithm.createShortUrl(shortUrl);
        } while (urlMapRepository.existsByShortUrl(shortUrl));

        UrlMap save = urlMapRepository.save(UrlMap.builder()
                .origin(urlCreateRequestDto.getOrigin())
                .shortUrl(shortUrl).build());
        return ResponseEntity.ok().body(save.getShortUrl());
    }

    public String redirect(String shortUrl) {
        Optional<UrlMap> urlMap = urlMapRepository.findByShortUrl(shortUrl);

        if (!urlMap.isPresent())
            throw new PageNotFoundException();
        return "redirect:" + urlMap.get().getOrigin();
    }
}
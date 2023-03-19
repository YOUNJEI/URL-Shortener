package com.url.urlshortener.service;

import com.url.urlshortener.controller.dto.CollectInformationDto;
import com.url.urlshortener.controller.dto.UrlCreateRequestDto;
import com.url.urlshortener.entity.UrlMap;
import com.url.urlshortener.entity.VisitHistory;
import com.url.urlshortener.entity.VisitHistoryId;
import com.url.urlshortener.exception.PageNotFoundException;
import com.url.urlshortener.repository.UrlMapRepository;
import com.url.urlshortener.repository.VisitHistoryRepository;
import com.url.urlshortener.utility.ShortenerAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Transactional
@Service
public class UrlService {
    private final UrlMapRepository urlMapRepository;
    private final VisitHistoryRepository visitHistoryRepository;
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

    public String redirect(String shortUrl, HttpServletRequest request) {
        Optional<UrlMap> urlMap = urlMapRepository.findByShortUrl(shortUrl);

        if (!urlMap.isPresent())
            throw new PageNotFoundException();

        // Async
        CompletableFuture.runAsync(() -> collectInformation(shortUrl, CollectInformationDto.builder()
                .userAgent(request.getHeader("User-Agent"))
                .ipAddress(request.getRemoteAddr())
                .language(request.getLocale().getLanguage()).build()));

        return "redirect:" + urlMap.get().getOrigin();
    }

    private void collectInformation(String shortUrl, CollectInformationDto collectInformationDto) {
        Long sequence = visitHistoryRepository.countById(shortUrl);

        VisitHistoryId visitHistoryId = VisitHistoryId.builder()
                .shortUrl(shortUrl)
                .id(sequence + 1).build();

        VisitHistory visitHistory = visitHistoryRepository.save(VisitHistory.builder()
                .id(visitHistoryId)
                .browser(collectInformationDto.getUserAgent().substring(0, 10))
                .location(collectInformationDto.getIpAddress())
                .language(collectInformationDto.getLanguage()).build());
    }
}

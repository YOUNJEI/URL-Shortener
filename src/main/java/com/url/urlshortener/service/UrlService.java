package com.url.urlshortener.service;

import com.url.urlshortener.controller.dto.CollectInformationDto;
import com.url.urlshortener.controller.dto.UrlCreateRequestDto;
import com.url.urlshortener.controller.dto.UrlCreateResponseDto;
import com.url.urlshortener.entity.UrlMap;
import com.url.urlshortener.entity.UrlMapId;
import com.url.urlshortener.entity.VisitHistory;
import com.url.urlshortener.entity.VisitHistoryId;
import com.url.urlshortener.exception.CustomException;
import com.url.urlshortener.exception.CustomExceptionEnum;
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
    private final RedisService redisService;
    private final HttpServletRequest httpServletRequest;

    public ResponseEntity<UrlCreateResponseDto> createUrl(UrlCreateRequestDto urlCreateRequestDto) {
        if (!urlCreateRequestDto.isInvalidate())
            throw new CustomException(CustomExceptionEnum.URL_INVALID);

        UrlMapId urlMapId = urlCreateRequestDto.getUrlMapId();
        Optional<UrlMap> urlMap = urlMapRepository.findById(urlMapId);
        if (urlMap.isPresent())
            return ResponseEntity.ok().body(new UrlCreateResponseDto(urlMap.get().getShortUrl(), httpServletRequest));

        // create unique short URL
        String shortUrl = urlMapId.getOrigin() + ":" + urlMapId.getOwner();
        do {
            shortUrl = shortenerAlgorithm.createShortUrl(shortUrl);
        } while (urlMapRepository.existsByShortUrl(shortUrl));

        UrlMap save = urlMapRepository.save(UrlMap.builder()
                .urlMapId(urlMapId)
                .shortUrl(shortUrl).build());
        return ResponseEntity.ok().body(new UrlCreateResponseDto(save.getShortUrl(), httpServletRequest));
    }

    public String redirect(String shortUrl, HttpServletRequest request) {
        String cache = redisService.getValue("short::" + shortUrl);
        if (cache != null) {
            CompletableFuture.runAsync(() -> collectInformation(shortUrl, CollectInformationDto.builder()
                    .userAgent(request.getHeader("User-Agent"))
                    .ipAddress(request.getRemoteAddr())
                    .language(request.getLocale().getLanguage()).build()));

            return "redirect:" + cache;
        }

        Optional<UrlMap> urlMap = urlMapRepository.findByShortUrl(shortUrl);
        if (!urlMap.isPresent())
            throw new PageNotFoundException();

        // Async
        CompletableFuture.runAsync(() -> collectInformation(shortUrl, CollectInformationDto.builder()
                .userAgent(request.getHeader("User-Agent"))
                .ipAddress(request.getRemoteAddr())
                .language(request.getLocale().getLanguage()).build()));

        assert urlMap.get().getId() != null;
        redisService.setValue("short::" + urlMap.get().getShortUrl(), urlMap.get().getId().getOrigin(), 3);

        return "redirect:" + urlMap.get().getId().getOrigin();
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

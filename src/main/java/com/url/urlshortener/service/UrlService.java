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
import com.url.urlshortener.utility.Geoip;
import com.url.urlshortener.utility.ShortenerAlgorithm;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final Geoip geoip;

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

    public ResponseEntity<String> deleteUrl(String shortUrl) {
        UrlMap urlMap = urlMapRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new CustomException(CustomExceptionEnum.SHORT_URL_NOT_FOUND));

        KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername();
        if (!urlMap.getId().getOwner().equals(userName))
            throw new CustomException(CustomExceptionEnum.SHORT_URL_PERMISSION_DENIED);
        redisService.deleteKey("short::" + urlMap.getShortUrl());
        urlMapRepository.delete(urlMap);

        return ResponseEntity.ok().body(null);
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
        UrlMap urlMap = UrlMap.builder()
                .shortUrl(shortUrl).build();

        Long sequence = visitHistoryRepository.countById_UrlMap(urlMap);

        VisitHistoryId visitHistoryId = VisitHistoryId.builder()
                .urlMap(urlMap)
                .id(sequence + 1).build();

        visitHistoryRepository.save(VisitHistory.builder()
                .id(visitHistoryId)
                .browser(parseBrowser(collectInformationDto.getUserAgent()))
                .location(geoip.getLocation(collectInformationDto.getIpAddress()))
                .language(collectInformationDto.getLanguage()).build());
    }

    private String parseBrowser(String userAgent) {
        if (userAgent == null)
            return "Unknown";

        if (userAgent.contains("Chrome"))
            return "Chrome";
        else if (userAgent.contains("Safari"))
            return "Safari";
        else if (userAgent.contains("Trident"))
            return "IE";
        else if (userAgent.contains("Opera"))
            return "Opera";
        else if (userAgent.contains("Edge"))
            return "Edge";
        else if (userAgent.contains("FireFox"))
            return "FireFox";
        else if (userAgent.contains("iPhone"))
            return "iPhone";
        else if (userAgent.contains("Android"))
            return "Android";
        return "Unknown";
    }
}

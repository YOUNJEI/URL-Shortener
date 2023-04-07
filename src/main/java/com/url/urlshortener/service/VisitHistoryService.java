package com.url.urlshortener.service;

import com.url.urlshortener.controller.dto.UrlListResponseInterface;
import com.url.urlshortener.controller.dto.UrlVisitorDetailResponseDto;
import com.url.urlshortener.entity.UrlMap;
import com.url.urlshortener.entity.VisitHistory;
import com.url.urlshortener.exception.CustomException;
import com.url.urlshortener.exception.CustomExceptionEnum;
import com.url.urlshortener.repository.UrlMapRepository;
import com.url.urlshortener.repository.VisitHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class VisitHistoryService {
    private final UrlMapRepository urlMapRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    public List<UrlListResponseInterface> getUrlList() {
        String userName = getUserName();
        return urlMapRepository.findDistinctUrlMapList(userName);
    }

    public UrlVisitorDetailResponseDto getDetail(String shortUrl) {
        String userName = getUserName();

        UrlMap urlMap = urlMapRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new CustomException(CustomExceptionEnum.SHORT_URL_NOT_FOUND));

        assert urlMap.getId() != null;
        if (!urlMap.getId().getOwner().equals(userName))
            throw new CustomException(CustomExceptionEnum.SHORT_URL_PERMISSION_DENIED);

        List<VisitHistory> visitHistoryList = visitHistoryRepository.findAllById_UrlMap_ShortUrl(shortUrl); // Query 2
        return new UrlVisitorDetailResponseDto(urlMap, visitHistoryList);
    }

    private String getUserName() {
        KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername();
        return userName;
    }
}

package com.url.urlshortener.service;

import com.url.urlshortener.controller.dto.UrlListResponseInterface;
import com.url.urlshortener.repository.UrlMapRepository;
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

    public List<UrlListResponseInterface> getUrlList() {
        KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername();
        return urlMapRepository.findDistinctUrlMapList(userName);
    }
}

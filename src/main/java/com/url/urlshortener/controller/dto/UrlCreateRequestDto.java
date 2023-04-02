package com.url.urlshortener.controller.dto;

import com.url.urlshortener.entity.UrlMapId;
import lombok.Data;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
public class UrlCreateRequestDto {
    private String origin;

    public boolean isInvalidate() {
        if (!this.origin.matches("(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)"))
            return false;
        if (!this.origin.matches("^https?://.*"))
            origin = "http://" + origin;
        return true;
    }

    public UrlMapId getUrlMapId() {
        KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername();
        return UrlMapId.builder()
                .origin(origin)
                .owner(userName).build();
    }
}

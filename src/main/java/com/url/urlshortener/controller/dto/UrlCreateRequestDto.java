package com.url.urlshortener.controller.dto;

import lombok.Data;

@Data
public class UrlCreateRequestDto {
    private String origin;

    public boolean isInvalidate() {
        return this.origin.matches("(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
    }
}

package com.url.urlshortener.controller.dto;

import lombok.Getter;

@Getter
public class UrlCreateResponseDto {
    public String shortUrl;

    public UrlCreateResponseDto(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}

package com.url.urlshortener.controller.dto;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Getter
public class UrlCreateResponseDto {
    public String shortUrl;

    public UrlCreateResponseDto(String shortUrl, HttpServletRequest httpServletRequest) {
        this.shortUrl = httpServletRequest.getServerName() + "s/" + shortUrl;
    }
}

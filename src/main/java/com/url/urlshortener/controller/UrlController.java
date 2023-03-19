package com.url.urlshortener.controller;

import com.url.urlshortener.controller.dto.UrlCreateRequestDto;
import com.url.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/api/v1/url")
    public ResponseEntity<String> createUrl(@RequestBody UrlCreateRequestDto urlCreateRequestDto) {
        return urlService.createUrl(urlCreateRequestDto);
    }

    @GetMapping("/{short}")
    public String redirect(@PathVariable("short") String shortUrl) {
        return urlService.redirect(shortUrl);
    }
}
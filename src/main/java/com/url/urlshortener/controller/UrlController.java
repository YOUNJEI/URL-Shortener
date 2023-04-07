package com.url.urlshortener.controller;

import com.url.urlshortener.controller.dto.UrlCreateRequestDto;
import com.url.urlshortener.controller.dto.UrlCreateResponseDto;
import com.url.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/api/v1/url")
    public ResponseEntity<UrlCreateResponseDto> createUrl(@RequestBody UrlCreateRequestDto urlCreateRequestDto) {
        return urlService.createUrl(urlCreateRequestDto);
    }

    @DeleteMapping("/api/v1/url/{short}")
    public ResponseEntity<String> deleteUrl(@PathVariable("short") String shortUrl) {
        return urlService.deleteUrl(shortUrl);
    }

    @GetMapping("/{short}")
    public String redirect(@PathVariable("short") String shortUrl, HttpServletRequest request) {
        return urlService.redirect(shortUrl, request);
    }

    @GetMapping("/page/home")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/page/create-url")
    public String createUrlPage() {
        return "create-url";
    }

    @GetMapping("/")
    public String redirectHome() {
        return "redirect:/page/home";
    }
}

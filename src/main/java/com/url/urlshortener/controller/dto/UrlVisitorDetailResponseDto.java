package com.url.urlshortener.controller.dto;

import com.url.urlshortener.entity.UrlMap;
import com.url.urlshortener.entity.VisitHistory;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UrlVisitorDetailResponseDto {
    private String origin;
    private String shortUrl;
    private List<UrlVisitorDto> visitors;

    public UrlVisitorDetailResponseDto(UrlMap urlMap, List<VisitHistory> visitHistoryList) {
        this.origin = urlMap.getId().getOrigin();
        this.shortUrl = urlMap.getShortUrl();
        this.visitors = visitHistoryList.stream()
                .map(UrlVisitorDto::new).collect(Collectors.toList());
    }
}

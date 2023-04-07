package com.url.urlshortener.controller.dto;

import com.url.urlshortener.entity.VisitHistory;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UrlVisitorDto {
    private Long id;
    private String browser;
    private String language;
    private String location;
    private Timestamp visited;

    public UrlVisitorDto(VisitHistory visitHistory) {
        assert visitHistory.getId() != null;
        this.id = visitHistory.getId().getId();
        this.browser = visitHistory.getBrowser();
        this.language = visitHistory.getLanguage();
        this.location = visitHistory.getLocation();
        this.visited = visitHistory.getVisited();
    }
}

package com.url.urlshortener.controller.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class HistoryResponseDto {
    private String origin;
    private String shortUrl;
    private Long visitors;
    private Timestamp createdTime;
}

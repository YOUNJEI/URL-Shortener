package com.url.urlshortener.controller.dto;

import java.sql.Timestamp;

public interface UrlListResponseInterface {
    String getOrigin();
    String getShortUrl();
    int getVisitors();
    Timestamp getCreated();
}

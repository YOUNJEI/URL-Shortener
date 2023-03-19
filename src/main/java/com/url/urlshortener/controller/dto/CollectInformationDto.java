package com.url.urlshortener.controller.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CollectInformationDto {
    String userAgent;
    String ipAddress;
    String language;
}

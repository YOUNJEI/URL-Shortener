package com.url.urlshortener.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Data
@Builder
public class CustomExceptionResponse {
    private int status;
    private String code;
    private String message;
    private LocalDateTime time;

    public static ResponseEntity<CustomExceptionResponse> toResponseEntity(CustomExceptionEnum e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(CustomExceptionResponse.builder()
                        .status(e.getHttpStatus().value())
                        .code(e.name())
                        .message(e.getMessage())
                        .time(LocalDateTime.now())
                        .build());
    }
}
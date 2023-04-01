package com.url.urlshortener.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CustomExceptionEnum {
    URL_INVALID(HttpStatus.BAD_REQUEST, "올바른 URL 주소를 입력해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}

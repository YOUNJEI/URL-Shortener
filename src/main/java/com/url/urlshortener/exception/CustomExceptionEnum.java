package com.url.urlshortener.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CustomExceptionEnum {
    URL_INVALID(HttpStatus.BAD_REQUEST, "올바른 URL 주소를 입력해주세요."),
    SHORT_URL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 단축 URL 정보를 찾을 수 없습니다."),
    SHORT_URL_PERMISSION_DENIED(HttpStatus.UNAUTHORIZED, "해당 단축 URL 정보에 접근할 권한이 없습니다."),
    CACHE_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "캐싱 오류입니다. 서버로 문의주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}

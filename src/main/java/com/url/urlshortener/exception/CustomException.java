package com.url.urlshortener.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private CustomExceptionEnum customExceptionEnum;
}

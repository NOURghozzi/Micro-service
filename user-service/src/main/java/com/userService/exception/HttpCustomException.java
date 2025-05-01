package com.userService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpCustomException extends RuntimeException {
    private final HttpStatus status;
    private final int statusCode;

    public HttpCustomException(String message, HttpStatus status, int statusCode) {
        super(message);
        this.status = status;
        this.statusCode = statusCode;
    }

}

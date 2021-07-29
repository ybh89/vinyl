package com.hansung.vinyl.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoRefreshTokenException extends RuntimeException {
    public static final String MESSAGE = "access token 이 만료되어 refresh token 이 필요합니다. refresh token 을 보내주거나, 다시 로그인 요청하세요.";

    public NoRefreshTokenException() {
        super(MESSAGE);
    }

    public NoRefreshTokenException(String message) {
        super(message);
    }
}

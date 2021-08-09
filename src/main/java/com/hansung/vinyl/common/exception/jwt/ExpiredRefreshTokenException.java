package com.hansung.vinyl.common.exception.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpiredRefreshTokenException extends RuntimeException {
    public static final String MESSAGE = "access token, refresh token 모두 만료되었습니다. 로그인을 해주세요.";

    public ExpiredRefreshTokenException() {
        super(MESSAGE);
    }

    public ExpiredRefreshTokenException(String message) {
        super(message);
    }
}

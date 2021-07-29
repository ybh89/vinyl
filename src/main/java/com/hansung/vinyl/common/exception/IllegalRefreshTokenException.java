package com.hansung.vinyl.common.exception;

public class IllegalRefreshTokenException extends RuntimeException {
    public static final String MESSAGE = "refresh token 이 유효하지 않습니다.";

    public IllegalRefreshTokenException() {
        super(MESSAGE);
    }

    public IllegalRefreshTokenException(String message) {
        super(message);
    }
}

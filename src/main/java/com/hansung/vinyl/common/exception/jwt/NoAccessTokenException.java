package com.hansung.vinyl.common.exception.jwt;

public class NoAccessTokenException extends RuntimeException {
    public static final String MESSAGE = "access token 이 존재하지 않습니다.";

    public NoAccessTokenException() {
        super(MESSAGE);
    }

    public NoAccessTokenException(String message) {
        super(message);
    }
}

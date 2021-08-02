package com.hansung.vinyl.common.exception;

public class CannotReadImageFileException extends RuntimeException {
    public static final String MESSAGE = "이미지 파일 읽기에 실패했습니다.";

    public CannotReadImageFileException() {
        super(MESSAGE);
    }

    public CannotReadImageFileException(String message) {
        super(message);
    }

    public CannotReadImageFileException(Throwable cause) {
        super(cause);
    }
}

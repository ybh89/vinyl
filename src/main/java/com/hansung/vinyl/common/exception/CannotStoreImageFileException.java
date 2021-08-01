package com.hansung.vinyl.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotStoreImageFileException extends RuntimeException {
    public static final String MESSAGE = "이미지 파일 저장에 실패했습니다.";

    public CannotStoreImageFileException() {
        super(MESSAGE);
    }

    public CannotStoreImageFileException(String message) {
        super(message);
    }
}

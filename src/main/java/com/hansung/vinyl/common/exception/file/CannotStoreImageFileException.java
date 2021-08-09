package com.hansung.vinyl.common.exception.file;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotStoreImageFileException extends FileException {
    public static final String MESSAGE = "이미지 파일 저장에 실패했습니다.";

    public CannotStoreImageFileException(Throwable throwable, String fileName, String path) {
        super(MESSAGE, throwable, fileName, path);
    }
}

package com.hansung.vinyl.common.exception.file;

public class CannotReadImageFileException extends FileException {
    public static final String MESSAGE = "이미지 파일 읽기에 실패했습니다.";

    public CannotReadImageFileException(String fileName, String path) {
        super(MESSAGE, fileName, path);
    }
}

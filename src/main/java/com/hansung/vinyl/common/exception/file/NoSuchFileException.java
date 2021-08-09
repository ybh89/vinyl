package com.hansung.vinyl.common.exception.file;

public class NoSuchFileException extends FileException {
    private static final String MESSAGE = "파일이 존재하지 않습니다.";

    public NoSuchFileException(String fileName, String path) {
        super(MESSAGE, fileName, path);
    }
}

package com.hansung.vinyl.common.exception.file;

public class NotSupportedFileExtensionException extends FileException {
    private static final String MESSAGE = "지원하지 않는 파일 확장자 입니다.";
    public NotSupportedFileExtensionException(String fileName, String path) {
        super(MESSAGE, fileName, path);
    }
}

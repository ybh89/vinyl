package com.hansung.vinyl.common.exception;

import lombok.Getter;

@Getter
public abstract class FileException extends RuntimeException {
    private String fileName;
    private String path;

    public FileException(String message, String fileName, String path) {
        super(message);
        this.fileName = fileName;
        this.path = path;
    }
}

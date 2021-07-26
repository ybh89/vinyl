package com.hansung.vinyl.common.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

public class Error {
    private String field;
    private String rejectedValue;
    private String objectName;
    private String message;
    private HttpStatus httpStatus;

    @Builder
    public Error(String field, String rejectedValue, String objectName, String message, HttpStatus httpStatus) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.objectName = objectName;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

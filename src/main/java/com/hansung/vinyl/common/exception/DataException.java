package com.hansung.vinyl.common.exception;

import lombok.Getter;

@Getter
public abstract class DataException extends RuntimeException {
    private String field;
    private String rejectedValue;
    private String objectName;

    public DataException(String message, String field, String rejectedValue, String objectName) {
        super(message);
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.objectName = objectName;
    }
}

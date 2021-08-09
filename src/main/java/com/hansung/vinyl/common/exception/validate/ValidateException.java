package com.hansung.vinyl.common.exception.validate;

public class ValidateException extends RuntimeException {
    private String field;
    private Object rejectedValue;
    private String objectName;

    public ValidateException(String message, String field, Object rejectedValue, String objectName) {
        super(message);
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.objectName = objectName;
    }
}

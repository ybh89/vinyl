package com.hansung.vinyl.common.exception.validate;

public class NullException extends ValidateException {
    private static final String MESSAGE = "Null 값 입니다.";

    public NullException(String field, String objectName) {
        super(MESSAGE + "(" + field + ")", field, null, objectName);
    }
}

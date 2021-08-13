package com.hansung.vinyl.common.exception.validate;

public class OutOfBoundsException extends ValidateException {
    private static final String MESSAGE = "범위를 벗어났습니다.";

    public OutOfBoundsException(String field, Object rejectedValue, String objectName) {
        super(MESSAGE + "(" + field + " = " + rejectedValue + ")", field, rejectedValue, objectName);
    }
}

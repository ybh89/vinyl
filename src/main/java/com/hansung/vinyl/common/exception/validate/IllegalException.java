package com.hansung.vinyl.common.exception.validate;

public class IllegalException extends ValidateException {
    private static final String MESSAGE = "잘못된 데이터입니다.";

    public IllegalException(String field, Object rejectedValue, String objectName) {
        super(MESSAGE  + "(" + field + " = " + rejectedValue + ")", field, rejectedValue, objectName);
    }
}

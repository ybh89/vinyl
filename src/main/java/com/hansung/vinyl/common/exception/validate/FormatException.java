package com.hansung.vinyl.common.exception.validate;

public class FormatException extends ValidateException {
    private static final String MESSAGE = "형식이 맞지 않습니다.";

    public FormatException(String field, Object rejectedValue, String objectName) {
        super(MESSAGE, field, rejectedValue, objectName);
    }
}

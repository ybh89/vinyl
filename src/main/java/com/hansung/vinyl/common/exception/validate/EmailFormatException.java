package com.hansung.vinyl.common.exception.validate;

public class EmailFormatException extends ValidateException {
    private static final String MESSAGE = "이메일 형식이 맞지 않습니다.";
    public EmailFormatException(Object rejectedValue, String objectName) {
        super(MESSAGE, "email", rejectedValue, objectName);
    }
}

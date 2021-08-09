package com.hansung.vinyl.common.exception.validate;

public class BlankException extends ValidateException {
    private static final String MESSAGE = "입력한 데이터가 비어있습니다.";

    public BlankException(String field, Object rejectedValue, String objectName) {
        super(MESSAGE, field, rejectedValue, objectName);
    }
}

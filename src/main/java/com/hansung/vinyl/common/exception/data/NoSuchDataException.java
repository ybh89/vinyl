package com.hansung.vinyl.common.exception.data;

public class NoSuchDataException extends DataException {
    private static final String MESSAGE = "데이터가 존재하지 않습니다.";
    public NoSuchDataException(String field, Object rejectedValue, String objectName) {
        super(MESSAGE + "(" + field + " = " + rejectedValue + ")", field, rejectedValue, objectName);
    }

    public NoSuchDataException(String message, String field, Object rejectedValue, String objectName) {
        super(message, field, rejectedValue, objectName);
    }
}

package com.hansung.vinyl.common.exception;

public class NoSuchDataException extends DataException {
    private static final String MESSAGE = "데이터가 존재하지 않습니다.";
    public NoSuchDataException(String field, String rejectedValue, String objectName) {
        super(MESSAGE, field, rejectedValue, objectName);
    }
}

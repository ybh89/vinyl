package com.hansung.vinyl.common.exception;

public class DuplicateDataException extends DataException {
    private static final String MESSAGE = "중복된 데이터가 이미 존재합니다.";

    public DuplicateDataException(String field, String rejectedValue, String objectName) {
        super(MESSAGE, field, rejectedValue, objectName);
    }
}

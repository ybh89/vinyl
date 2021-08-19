package com.hansung.vinyl.common.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class Error implements Serializable {
    private String field;
    private Object rejectedValue;
    private String objectName;
    private String message;
    private HttpStatus httpStatus;
    private String exceptionType;
    private Exception exception;
    private LocalDateTime dateTime;

    @Builder
    public Error(String field, Object rejectedValue, String objectName, String message, HttpStatus httpStatus,
                 String exceptionType, Exception exception, LocalDateTime dateTime) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.objectName = objectName;
        this.message = message;
        this.httpStatus = httpStatus;
        this.exceptionType = exceptionType;
        this.exception = exception;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Error{" +
                "field='" + field + '\'' +
                ", rejectedValue=" + rejectedValue +
                ", objectName='" + objectName + '\'' +
                ", message='" + message + '\'' +
                ", httpStatus=" + httpStatus +
                ", exceptionType='" + exceptionType + '\'' +
                '}';
    }
}

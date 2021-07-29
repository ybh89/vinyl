package com.hansung.vinyl.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class Error implements Serializable {
    private String field;
    private String rejectedValue;
    private String objectName;
    private String message;
    private HttpStatus httpStatus;
    private String exceptionType;
    private Exception exception;

    @Builder
    public Error(String field, String rejectedValue, String objectName, String message, HttpStatus httpStatus, String exceptionType, Exception exception) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.objectName = objectName;
        this.message = message;
        this.httpStatus = httpStatus;
        this.exceptionType = exceptionType;
        this.exception = exception;
    }
}

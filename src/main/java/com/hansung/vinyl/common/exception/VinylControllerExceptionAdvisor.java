package com.hansung.vinyl.common.exception;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class VinylControllerExceptionAdvisor {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleRuntimeException(RuntimeException exception) {
        log.error("서버 개발자 확인이 필요합니다.", exception);
        Error error = Error.builder()
                .httpStatus(INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .exceptionType(exception.getClass().getTypeName())
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("필드 에러가 발생했습니다.", exception);
        FieldError fieldError = exception.getFieldError();
        Error error = Error.builder()
                .httpStatus(BAD_REQUEST)
                .field(fieldError.getField())
                .objectName(fieldError.getObjectName())
                .rejectedValue(fieldError.getRejectedValue().toString())
                .message(fieldError.getDefaultMessage())
                .build();
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

}

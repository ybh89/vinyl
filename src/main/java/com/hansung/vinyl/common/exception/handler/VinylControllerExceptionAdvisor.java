package com.hansung.vinyl.common.exception.handler;

import com.hansung.vinyl.common.exception.AuthorizationException;
import com.hansung.vinyl.common.exception.DataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class VinylControllerExceptionAdvisor {
    private final MessageSource messageSource;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleRuntimeException(RuntimeException exception) {
        log.error("서버 개발자 확인이 필요", exception);
        Error error = Error.builder()
                .httpStatus(INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .exceptionType(exception.getClass().getTypeName())
                .dateTime(LocalDateTime.now())
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<Error>> handleBindException(BindException exception, Locale locale) {
        log.error("필드 에러가 발생", exception);
        List<FieldError> fieldErrors = exception.getFieldErrors();

        List<Error> errors = fieldErrors.stream()
                .map(fieldError -> Error.builder()
                        .httpStatus(BAD_REQUEST)
                        .field(fieldError.getField())
                        .objectName(fieldError.getObjectName())
                        .rejectedValue(fieldError.getRejectedValue())
                        .message(messageSource.getMessage(fieldError, locale))
                        .dateTime(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.status(BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<Error> handleDataException(DataException exception) {
        log.error("데이터 에러 발생", exception);
        Error error = Error.builder()
                .httpStatus(INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .objectName(exception.getObjectName())
                .field(exception.getField())
                .rejectedValue(exception.getRejectedValue())
                .exceptionType(exception.getClass().getTypeName())
                .dateTime(LocalDateTime.now())
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Error> handleAuthorizationException(AuthorizationException exception) {
        log.error("권한 에러 발생", exception);
        Error error = Error.builder()
                .httpStatus(FORBIDDEN)
                .message(exception.getMessage())
                .exceptionType(exception.getClass().getTypeName())
                .dateTime(LocalDateTime.now())
                .build();
        return ResponseEntity.status(FORBIDDEN).body(error);
    }
}

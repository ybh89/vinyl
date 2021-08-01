package com.hansung.vinyl.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class VinylControllerExceptionAdvisor {
    private final MessageSource messageSource;

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
    public ResponseEntity<List<Error>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                             Locale locale) {
        log.error("필드 에러가 발생했습니다.", exception);
        List<FieldError> fieldErrors = exception.getFieldErrors();

        List<Error> errors = fieldErrors.stream()
                .map(fieldError -> Error.builder()
                        .httpStatus(BAD_REQUEST)
                        .field(fieldError.getField())
                        .objectName(fieldError.getObjectName())
                        .rejectedValue(fieldError.getRejectedValue().toString())
                        .message(messageSource.getMessage(fieldError, locale))
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.status(BAD_REQUEST).body(errors);
    }

}

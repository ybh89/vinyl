package com.hansung.vinyl.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
public class VinylControllerExceptionAdvisor {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleRuntimeException(RuntimeException exception) {
        log.error("RuntimeException", exception);
        Error error = Error.builder()
                .httpStatus(INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Error> handleAuthorizationException(AuthorizationException exception) {
        Error error = Error.builder()
                .httpStatus(UNAUTHORIZED)
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(UNAUTHORIZED).body(error);
    }
}

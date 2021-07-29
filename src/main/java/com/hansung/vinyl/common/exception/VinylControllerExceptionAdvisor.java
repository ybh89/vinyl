package com.hansung.vinyl.common.exception;

import io.jsonwebtoken.*;
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
        log.error("서버 개발자 확인이 필요합니다.", exception);
        Error error = Error.builder()
                .httpStatus(INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Error> handleAuthorizationException(AuthorizationException exception) {
        System.out.println("AuthorizationException!!!!");
        Error error = Error.builder()
                .httpStatus(UNAUTHORIZED)
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Error> handleJwtException(JwtException exception) {
        String message = getJWTErrorMessage(exception);
        Error error = Error.builder()
                .httpStatus(UNAUTHORIZED)
                .message(message)
                .exception(exception)
                .build();
        return ResponseEntity.status(UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<Error> handleExpiredJWTAccessAndRefreshException(ExpiredRefreshTokenException exception) {
        Error error = Error.builder()
                .httpStatus(UNAUTHORIZED)
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(NoRefreshTokenException.class)
    public ResponseEntity<Error> handleNoRefreshTokenException(NoRefreshTokenException exception) {
        Error error = Error.builder()
                .httpStatus(UNAUTHORIZED)
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(UNAUTHORIZED).body(error);
    }

    private String getJWTErrorMessage(JwtException exception) {
        String message = "유효하지않은 JWT 토큰입니다.";

        if (exception instanceof ExpiredJwtException) {
            message = "만료된 JWT 토큰입니다.";
        }
        if (exception instanceof MalformedJwtException) {
            message = "JWT 의 구조적인 문제가 있습니다.";
        }
        if (exception instanceof ClaimJwtException) {
            message = "JWT 권한 검사에 실패했습니다.";
        }
        if (exception instanceof SignatureException) {
            message = "JWT 의 시그니처 검사에 실패했습니다.";
        }
        if (exception instanceof UnsupportedJwtException) {
            message = "지원하지않는 JWT 형식입니다.";
        }
        return message;
    }
}

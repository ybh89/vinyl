package com.hansung.vinyl.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansung.vinyl.common.exception.handler.Error;
import com.hansung.vinyl.common.exception.jwt.ExpiredRefreshTokenException;
import com.hansung.vinyl.common.exception.jwt.IllegalRefreshTokenException;
import com.hansung.vinyl.common.exception.jwt.NoRefreshTokenException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger errLogger = LoggerFactory.getLogger("err");

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = "인증이 필요합니다. 로그인 해주세요.";

        Exception exception = (Exception) request.getAttribute("exception");
        if (Objects.nonNull(exception)) {
            message = getJWTErrorMessage(exception);
        }

        if (Objects.isNull(exception)) {
            exception = authException;
        }

        Error error = Error.builder()
                .httpStatus(UNAUTHORIZED)
                .message(message)
                .exceptionType(exception.getClass().getTypeName())
                .build();
        setResponse(response, error);
        errLogger.error(error.toString());
    }

    private void setResponse(HttpServletResponse response, Error error) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getWriter(), error);
    }

    private String getJWTErrorMessage(Exception exception) {
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
        if (exception instanceof NoRefreshTokenException) {
            message = NoRefreshTokenException.MESSAGE;
        }
        if (exception instanceof ExpiredRefreshTokenException) {
            message = ExpiredRefreshTokenException.MESSAGE;
        }
        if (exception instanceof IllegalRefreshTokenException) {
            message = IllegalRefreshTokenException.MESSAGE;
        }
        return message;
    }
}

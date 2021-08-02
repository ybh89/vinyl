package com.hansung.vinyl.security.infrastructure.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansung.vinyl.common.exception.handler.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String message = getAuthenticationErrorMessage(exception);
        Error error = Error.builder()
                .httpStatus(UNAUTHORIZED)
                .message(message)
                .build();
        log.error(message, exception);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), error);
    }

    private String getAuthenticationErrorMessage(AuthenticationException exception) {
        String message = "인증에 실패하였습니다.";
        if(exception instanceof BadCredentialsException) {
            message = "이메일 또는 비밀번호가 맞지 않습니다.";
        }
        if(exception instanceof DisabledException) {
            message = "계정이 잠긴 상태입니다.";
        }
        if(exception instanceof CredentialsExpiredException) {
            message = "비밀번호 유효기간이 만료되었습니다.";
        }
        return message;
    }
}

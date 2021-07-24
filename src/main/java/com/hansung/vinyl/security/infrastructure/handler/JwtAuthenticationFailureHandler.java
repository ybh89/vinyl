package com.hansung.vinyl.security.infrastructure.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@Slf4j
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errMsg = "권한이 없습니다.";

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if(exception instanceof BadCredentialsException) {
            errMsg = "이메일 또는 비밀번호가 맞지 않습니다.";
        } else if(exception instanceof DisabledException) {
            errMsg = "계정이 잠긴 상태입니다.";
        } else if(exception instanceof CredentialsExpiredException) {
            errMsg = "비밀번호 유효기간이 만료되었습니다.";
        }

        log.error(errMsg, exception);

        new ObjectMapper().writeValue(response.getWriter(), errMsg);
    }
}

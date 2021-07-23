package com.hansung.vinyl.auth.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansung.vinyl.auth.dto.LoginRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        LoginRequest loginRequest;

        try {
            loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("잘못된 로그인 요청입니다.");
        }

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword())
        );
    }
}

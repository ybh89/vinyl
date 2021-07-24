package com.hansung.vinyl.security.infrastructure.handler;

import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.account.infrastructure.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String jwtAccessToken = jwtProvider.createAccessToken(String.valueOf(user.getAccountId()));
        response.addHeader("access-token", jwtAccessToken);
    }
}

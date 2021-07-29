package com.hansung.vinyl.security.infrastructure.handler;

import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.security.infrastructure.filter.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);
        jwtProvider.saveRefreshToken(accessToken, refreshToken);

        Cookie refreshTokenCookie= new Cookie("refresh-token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");

        response.addCookie(refreshTokenCookie);
        response.addHeader("access-token", accessToken);
    }
}

package com.hansung.vinyl.security.handler;

import com.hansung.vinyl.account.domain.RefreshToken;
import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.security.filter.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String accessToken = jwtProvider.createAccessToken(user);
        RefreshToken refreshToken = jwtProvider.createRefreshToken(user);
        jwtProvider.saveRefreshToken(accessToken, refreshToken);

        Cookie refreshTokenCookie = setCookie(refreshToken);
        response.addCookie(refreshTokenCookie);
        response.addHeader("access-token", accessToken);
        response.addHeader("accountId", String.valueOf(user.getAccountId()));
    }

    private Cookie setCookie(RefreshToken refreshToken) {
        Cookie refreshTokenCookie= new Cookie("refresh-token", refreshToken.value());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        return refreshTokenCookie;
    }
}

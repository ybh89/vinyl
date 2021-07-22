package com.hansung.vinyl.auth.security.filter;

import com.hansung.vinyl.auth.security.AccessToken;
import com.hansung.vinyl.auth.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final String JWT_PREFIX = "Bearer";

    private final JwtProvider jwtProvider;
    private final Environment environment;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String sAccessToken = getAccessToken(request);
        if(!StringUtils.hasText(sAccessToken) || !sAccessToken.startsWith(JWT_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        AccessToken accessToken = new AccessToken(sAccessToken, environment.getProperty(JwtProvider.JWT_KEY_PROPERTY_NAME));
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String getAccessTokenString(HttpServletRequest request) {
        String sAccessToken;
        try {
            sAccessToken = getAccessToken(request);
            validateHasText(sAccessToken);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("access token 을 변환할 수 없습니다.");
        }
        return sAccessToken;
    }

    private void validateHasText(String accessToken) throws IllegalArgumentException {
        if(!StringUtils.hasText(accessToken)) {
            throw new IllegalArgumentException();
        }
    }

    private String getAccessToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION).replace(JWT_PREFIX, "");
    }
}

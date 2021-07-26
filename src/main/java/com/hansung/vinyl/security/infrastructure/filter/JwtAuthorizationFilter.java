package com.hansung.vinyl.security.infrastructure.filter;

import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.common.exception.AuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String credentials = AuthorizationExtractor.extract(request);
        System.out.println("credentials = " + credentials);
        if (Objects.isNull(credentials) || credentials.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtProvider.validateToken(credentials)) {
            throw new AuthorizationException("JWT 유효성 검사를 실패하였습니다.");
        }

        Authentication authentication = jwtProvider.getAuthentication(credentials);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("getName = " + authentication1.getName());
        System.out.println("getAuthorities = " + authentication1.getAuthorities());

        filterChain.doFilter(request, response);
    }
}

package com.hansung.vinyl.security.infrastructure.filter;

import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.common.exception.ExpiredRefreshTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 익명 사용자는 다음 필터로 이동하여 인가처리 필터에서 처리됨.
 * 토큰 검증 실패자는 익명 사용자 취급하여 request 에 exception 전달하고 entry point 에서 예외처리.
 *
 * case1: access token과 refresh token 모두가 만료된 경우 -> 에러 발생
 * case2: access token은 만료됐지만, refresh token은 유효한 경우 ->  access token 재발급(refresh token 검증 필요)
 * case3: access token은 유효하지만, refresh token은 만료된 경우 ->  refresh token 재발급
 * case4: accesss token과 refresh token 모두가 유효한 경우 -> 다음 필더로
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = jwtProvider.getAccessToken(request);
        boolean isExpiredAccessToken = false;
        try {
            jwtProvider.validateAccessToken(accessToken);
        } catch (ExpiredJwtException exception) {
            isExpiredAccessToken = true;
            log.info("[JwtAuthorizationFilter] accessToken 이 만료되었습니다.");
        } catch (Exception exception) {
            request.setAttribute("exception", exception);
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtProvider.getRefreshToken(request);
        log.info("[JwtAuthorizationFilter] accessToken = {}", accessToken);
        log.info("[JwtAuthorizationFilter] refreshToken = {}", refreshToken);

        if (isExpiredAccessToken) {
            try {
                jwtProvider.validateRefreshToken(refreshToken);
            } catch (Exception exception) {
                request.setAttribute("exception", exception);
                filterChain.doFilter(request, response);
                return;
            }

            accessToken = reissueAccessToken(response, refreshToken);
            log.info("[JwtAuthorizationFilter] new accessToken = {}", accessToken);
        }

        if (!isExpiredAccessToken) {
            try {
                if (Objects.nonNull(refreshToken) && !refreshToken.isEmpty()) {
                    jwtProvider.validateRefreshToken(refreshToken);
                }
            } catch (ExpiredRefreshTokenException exception) {
                String newRefreshToken = reissueRefreshToken(response, accessToken);
                log.info("[JwtAuthorizationFilter] new refreshToken = {}",  newRefreshToken);
            } catch (Exception exception) {
                request.setAttribute("exception", exception);
                filterChain.doFilter(request, response);
                return;
            }
        }

        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String reissueRefreshToken(HttpServletResponse response, String accessToken) throws IOException {
        User user = jwtProvider.findUser(accessToken);
        String newRefreshToken = jwtProvider.createRefreshToken(user);
        jwtProvider.saveRefreshToken(accessToken, newRefreshToken);
        Cookie refreshTokenCookie= new Cookie("refresh-token", newRefreshToken);
        //refreshTokenCookie.setHttpOnly(true);
        //refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
        return newRefreshToken;
    }

    private String reissueAccessToken(HttpServletResponse response, String refreshToken) throws IOException {
        User user = jwtProvider.findUser(refreshToken);
        String newAccessToken = jwtProvider.createAccessToken(user);
        response.setHeader("access-token", newAccessToken);
        return newAccessToken;
    }
}

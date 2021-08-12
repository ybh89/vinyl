package com.hansung.vinyl.security.filter;

import com.hansung.vinyl.account.application.AccountService;
import com.hansung.vinyl.account.domain.RefreshToken;
import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.common.exception.jwt.ExpiredRefreshTokenException;
import com.hansung.vinyl.common.exception.jwt.IllegalRefreshTokenException;
import com.hansung.vinyl.common.exception.jwt.NoAccessTokenException;
import com.hansung.vinyl.common.exception.jwt.NoRefreshTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private static final String AUTHORITIES_KEY = "authorities";
    public static final String JWT_EXCEPTION_KEY = "JwtException";

    private final AccountService accountService;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.access-expiration}")
    private long accessValidityInMilliseconds;
    @Value("${security.jwt.token.refresh-expiration}")
    private long refreshValidityInMilliseconds;

    public String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getAccountId()));
        String authorities = user.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(","));
        claims.put(AUTHORITIES_KEY, authorities);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public RefreshToken createRefreshToken(User user) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getAccountId()));
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshValidityInMilliseconds);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return new RefreshToken(refreshToken);
    }

    public String getAccessToken(HttpServletRequest request) {
        if (Objects.isNull(request.getHeader(HttpHeaders.AUTHORIZATION))) {
            return Strings.EMPTY;
        }
        return request.getHeader(HttpHeaders.AUTHORIZATION)
                .replace("Bearer", "");
    }

    public void saveRefreshToken(String accessToken, RefreshToken refreshToken) {
        Long accountId = getAccountId(accessToken);
        accountService.updateRefreshToken(accountId, refreshToken);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        User user = findUser(token);
        return new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
    }

    public String getRefreshToken(HttpServletRequest request) {
        if (Objects.isNull(request.getCookies())) {
            return Strings.EMPTY;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refresh-token"))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElse(Strings.EMPTY);
    }

    public Long getAccountId(String token) {
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    public User findUser(String token) {
        Long accountId = getAccountId(token);
        return  (User) accountService.loadUserById(accountId);
    }

    public void validateRefreshToken(String refreshToken) {
        if (Objects.isNull(refreshToken) || refreshToken.isEmpty()) {
            throw new NoRefreshTokenException();
        }

        try {
            validateToken(refreshToken);
        } catch (ExpiredJwtException exception) {
            throw new ExpiredRefreshTokenException();
        }

        User user = findUser(refreshToken);
        if (Objects.isNull(user.getRefreshToken()) || !user.getRefreshToken().equals(refreshToken)) {
            throw new IllegalRefreshTokenException();
        }
    }

    public void validateToken(String token) {
        parseClaims(token);
    }

    public void validateAccessToken(String accessToken) {
        if (Objects.isNull(accessToken) || accessToken.isEmpty()) {
            throw new NoAccessTokenException();
        }

        validateToken(accessToken);
    }
}

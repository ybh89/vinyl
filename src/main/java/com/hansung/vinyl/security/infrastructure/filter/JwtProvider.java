package com.hansung.vinyl.security.infrastructure.filter;

import com.hansung.vinyl.account.application.AccountService;
import com.hansung.vinyl.account.domain.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;

@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final AccountService accountService;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.access-expiration}")
    private long accessValidityInMilliseconds;
    @Value("${security.jwt.token.refresh-expiration}")
    private long refreshValidityInMilliseconds;

    private String createToken(String payload, long expiration) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createAccessToken(String payload) {
        return createToken(payload, accessValidityInMilliseconds);
    }

    public String createRefreshToken(String payload) {
        String refreshToken = createToken(payload, refreshValidityInMilliseconds);
        accountService.updateRefreshToken(Long.valueOf(payload), refreshToken);

        return refreshToken;
    }

    public String getPayload(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);

            return !claims.getExpiration()
                    .before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /*public Jws<Claims> validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException exception) {

        }
    }*/

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Long accountId = Long.valueOf(claims.getSubject());
        User user = (User) accountService.loadUserById(accountId);

        return new UsernamePasswordAuthenticationToken(user.getUsername(),null, user.getAuthorities());
    }
}

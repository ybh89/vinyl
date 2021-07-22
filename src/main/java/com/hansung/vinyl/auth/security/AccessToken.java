package com.hansung.vinyl.auth.security;

import com.hansung.vinyl.auth.domain.Account;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;
import java.util.Objects;

@Slf4j
public class AccessToken {
    public static final String AUTHORITIES_KEY = "roles";

    private String accessToken;

    public AccessToken(String accessToken, String key) {
        validateAccessToken(accessToken, key);
        this.accessToken = accessToken;
    }

    public AccessToken(Account account, String authorities, String key, Date expirationTime) {
        validateParameters(account, key, expirationTime);
        accessToken = Jwts.builder()
                .setSubject(String.valueOf(account.getId()))
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    private void validateParameters(Account account, String key, Date expirationTime) {
        if (Objects.isNull(account) || Objects.isNull(account.getId())) {
            throw new IllegalArgumentException("계정이 존재하지 않습니다.");
        }

        if (Strings.isEmpty(key)) {
            throw new IllegalArgumentException("JWT 키가 존재하지 않습니다.");
        }

        if (Objects.isNull(expirationTime)) {
            throw new IllegalArgumentException("access token 만료시간이 존재하지 않습니다.");
        }
    }

    public String value() {
        return accessToken;
    }

    public static boolean validateAccessToken(String token, String key) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}

package com.hansung.vinyl.auth.security;

import com.hansung.vinyl.auth.application.AccountService;
import com.hansung.vinyl.auth.domain.Account;
import com.hansung.vinyl.auth.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JwtProvider {
    public static final String JWT_KEY_PROPERTY_NAME = "token.secret";
    public static final String JWT_EXPIRATION_TIME_PROPERTY_NAME = "token.expiration_time";

    private final Environment environment;
    private final AccountService accountService;

    @Autowired
    public JwtProvider(Environment environment, AccountService accountService) {
        this.environment = environment;
        this.accountService = accountService;
    }

    public AccessToken createJwtAccessToken(Authentication authentication) {
        String username = getUsername(authentication);
        Account account = accountService.findAccountByEmail(username);
        String authorities = getAuthorities(authentication);
        String key = getKey();
        return new AccessToken(account, authorities, key, createExpirationTime());
    }

    public Authentication getAuthentication(AccessToken accessToken) {
        Claims claims = parseClaims(accessToken.value(), getKey());
        validateAuthorities(claims);
        Collection<? extends GrantedAuthority> authorities = getGrantedAuthorities(claims);
        User principal = createPrincipal(claims, authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private String getKey() {
        return environment.getProperty(JWT_KEY_PROPERTY_NAME);
    }

    private Date createExpirationTime() {
        return new Date(System.currentTimeMillis()
                + Long.parseLong(environment.getProperty(JWT_EXPIRATION_TIME_PROPERTY_NAME)));
    }

    private User createPrincipal(Claims claims, Collection<? extends GrantedAuthority> authorities) {
        return User.builder()
                .accountId(Long.valueOf(claims.getSubject()))
                .authorities(authorities)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(Claims claims) {
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        try {
            authorities = Arrays.stream(claims.get(AccessToken.AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException exception) {
        }
        return authorities;
    }

    private void validateAuthorities(Claims claims) {
        if (Objects.isNull(claims.get(AccessToken.AUTHORITIES_KEY))) {
            throw new IllegalArgumentException("권한 정보가 없는 토큰입니다.");
        }
    }

    private Claims parseClaims(String accessToken, String key) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private String getUsername(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getUsername();
    }
}

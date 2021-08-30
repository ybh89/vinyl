package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.authority.domain.Authority;
import com.hansung.vinyl.authority.domain.DefaultRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class User implements UserDetails, OAuth2User {
    private Long accountId;
    private RefreshToken refreshToken;
    private org.springframework.security.core.userdetails.User user;
    private Map<String, Object> attributes;

    public User(org.springframework.security.core.userdetails.User user) {
        this.user = user;
    }

    // 일반 로그인 사용자
    public User(Account account, List<Authority> authorities) {
        this.accountId = account.getId();
        this.refreshToken = account.getRefreshToken();
        this.user = new org.springframework.security.core.userdetails.User(account.getEmailValue(),
                account.getEncryptedPasswordValue(), authorities);
    }

    // Oauth 로그인 사용자
    public User(Account account, List<Authority> authorities, Map<String, Object> attributes) {
        this(account, authorities);
        this.attributes = attributes;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getRefreshToken() { return refreshToken.value(); }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public boolean hasSuperRole() {
        return user.getAuthorities().stream().anyMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equals(DefaultRole.ROLE_SUPER.name()));
    }

    @Override
    public String getName() {
        return null;
    }
}

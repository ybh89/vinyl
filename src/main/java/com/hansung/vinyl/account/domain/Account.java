package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.authority.domain.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Access(AccessType.FIELD)
@Table(uniqueConstraints={ @UniqueConstraint(name = "uk_account_email", columnNames = "email") })
@Entity
public class Account {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private EncryptedPassword encryptedPassword;

    private boolean deleted;

    @Embedded
    private RefreshToken refreshToken;

    @Embedded
    private AccountAuthorities accountAuthorities;

    @Builder
    public Account(Long id, String email, String encryptedPassword, List<Authority> authorities) {
        this.id = id;
        this.email = new Email(email);
        this.encryptedPassword = new EncryptedPassword(encryptedPassword);
        this.accountAuthorities = new AccountAuthorities(createAccountAuthorities(authorities));
    }

    public void delete() {
        deleted = true;
    }

    private List<AccountAuthority> createAccountAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> AccountAuthority.builder()
                        .account(this)
                        .authorityId(authority.getId())
                        .build())
                .collect(Collectors.toList());
    }

    public void changeAuthorities(List<Authority> authorities) {
        accountAuthorities.change(createAccountAuthorities(authorities));
    }

    public void updateRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public List<Long> getAuthorityIds() {
        return accountAuthorities.getAuthorityIds();
    }

    public void publishEvent(ApplicationEventPublisher publisher, Object event) {
        if (Objects.nonNull(publisher)) {
            publisher.publishEvent(event);
        }
    }

    public String getRefreshTokenValue() {
        if (Objects.isNull(refreshToken)) {
            return null;
        }
        return refreshToken.value();
    }

    public String getEncryptedPasswordValue() {
        if (Objects.isNull(encryptedPassword)) {
            return null;
        }
        return encryptedPassword.value();
    }

    public String getEmailValue() {
        if (Objects.isNull(email)) {
            return null;
        }
        return email.value();
    }

}

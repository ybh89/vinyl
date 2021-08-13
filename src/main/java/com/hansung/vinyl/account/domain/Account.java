package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.authority.domain.Authority;
import com.hansung.vinyl.common.domain.DateTimeAuditor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Access(FIELD)
@Table(uniqueConstraints={ @UniqueConstraint(name = "uk_account_email", columnNames = "email") })
@SecondaryTable(
        name = "account_authority",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "account_id")
)
@Entity
public class Account extends AbstractAggregateRoot<Account> {
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

    @Embedded
    private DateTimeAuditor dateTimeAuditor;

    @Builder
    private Account(String email, String encryptedPassword, List<Authority> authorities) {
        this.email = new Email(email);
        this.encryptedPassword = new EncryptedPassword(encryptedPassword);
        this.accountAuthorities = new AccountAuthorities(createAccountAuthorities(authorities));
        this.dateTimeAuditor = new DateTimeAuditor();
    }

    public static Account create(AccountInfo accountInfo, MemberInfo memberInfo) {
        Account account = Account.builder()
                .email(accountInfo.getEmail())
                .encryptedPassword(accountInfo.getEncryptedPassword())
                .authorities(accountInfo.getAuthorities())
                .build();
        account.registerEvent(new AccountCreatedEvent(account, memberInfo));
        return account;
    }

    public void delete() {
        deleted = true;
    }

    private List<AccountAuthority> createAccountAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new AccountAuthority(authority.getId()))
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

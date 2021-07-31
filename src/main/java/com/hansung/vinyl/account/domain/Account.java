package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.authority.domain.Authority;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints={ @UniqueConstraint(name = "uk_account_email", columnNames = "email") })
@Entity
public class Account {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, updatable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 150)
    private String password;

    private boolean deleted;

    @Column(length = 150)
    private String refreshToken;

    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AccountAuthority> accountAuthorities = new ArrayList<>();

    @Builder
    public Account(Long id, String email, String password, List<Authority> authorities, Join join,
                   ApplicationEventPublisher publisher) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.accountAuthorities.addAll(createAccountAuthorities(authorities));
        publisher.publishEvent(new AccountCreatedEvent(id, email, join.getName(), join.getPhone(), join.getGender()));
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
        accountAuthorities.clear();
        accountAuthorities.addAll(createAccountAuthorities(authorities));
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

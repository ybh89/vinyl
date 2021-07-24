package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.authority.domain.Authority;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Account {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String email;
    private String password;
    private boolean isDeleted;
    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<AccountAuthority> accountAuthorities = new ArrayList<>();

    @Builder
    public Account(Long id, String email, String password, List<Authority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.accountAuthorities.addAll(createAccountAuthorities(authorities));
    }

    public void delete() {
        isDeleted = true;
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
}

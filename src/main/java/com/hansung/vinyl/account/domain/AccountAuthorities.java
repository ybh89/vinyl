package com.hansung.vinyl.account.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Access(AccessType.FIELD)
@Embeddable
public class AccountAuthorities {
    @OneToMany(mappedBy = "account", cascade = {PERSIST, MERGE, REMOVE}, orphanRemoval = true, fetch = EAGER)
    private List<AccountAuthority> accountAuthorities = new ArrayList<>();

    public AccountAuthorities(List<AccountAuthority> accountAuthorities) {
        change(accountAuthorities);
    }

    public void change(List<AccountAuthority> accountAuthorities) {
        this.accountAuthorities.clear();
        this.accountAuthorities.addAll(accountAuthorities);
    }

    public List<Long> getAuthorityIds() {
        return accountAuthorities.stream()
                .map(AccountAuthority::getAuthorityId)
                .collect(Collectors.toList());
    }

    public List<AccountAuthority> getAccountAuthorities() {
        return accountAuthorities;
    }
}

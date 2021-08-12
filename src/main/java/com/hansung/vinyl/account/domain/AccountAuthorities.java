package com.hansung.vinyl.account.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.AccessType.*;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Access(FIELD)
@Embeddable
public class AccountAuthorities {
    @ElementCollection
    @CollectionTable(name = "account_authority", joinColumns = @JoinColumn(name = "account_id"))
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

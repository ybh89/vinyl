package com.hansung.vinyl.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    @OneToMany(mappedBy = "account")
    private List<AccountAuthority> accountAuthorities = new ArrayList<>();

    @Builder
    public Account(Long id, String email, String password, List<Authority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        if (Objects.nonNull(authorities)) {
            this.accountAuthorities = authorities.stream()
                    .map(authority -> AccountAuthority.builder()
                            .account(this)
                            .authority(authority).build())
                    .collect(Collectors.toList());
        }
    }

    public void delete() {
        isDeleted = true;
    }
}

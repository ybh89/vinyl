package com.hansung.vinyl.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(uniqueConstraints={ @UniqueConstraint(columnNames = { "account_id", "authority_id" }) })
@Entity
public class AccountAuthority {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    @JoinColumn(name = "account_id")
    @ManyToOne
    private Account account;
    @JoinColumn(name = "authority_id")
    @ManyToOne
    private Authority authority;

    @Builder
    public AccountAuthority(Long id, Account account, Authority authority) {
        this.id = id;
        this.account = account;
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountAuthority that = (AccountAuthority) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

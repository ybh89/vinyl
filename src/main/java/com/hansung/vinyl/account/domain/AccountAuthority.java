package com.hansung.vinyl.account.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Access(AccessType.FIELD)
@Table(uniqueConstraints={ @UniqueConstraint(name = "uk_account_authority", columnNames = { "account_id", "authority_id" }) })
@Entity
public class AccountAuthority {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(name = "fk_authority_account"))
    @ManyToOne
    private Account account;

    @JoinColumn(foreignKey = @ForeignKey(name = "fk_account_authority"))
    @Column(name = "authority_id")
    private Long authorityId;

    @Builder
    public AccountAuthority(Long id, Account account, Long authorityId) {
        this.id = id;
        this.account = account;
        this.authorityId = authorityId;
    }

    public void setAccount(Account account) {
        this.account = account;
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

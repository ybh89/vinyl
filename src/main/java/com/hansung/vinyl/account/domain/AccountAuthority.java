package com.hansung.vinyl.account.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Access(FIELD)
@Table(uniqueConstraints={ @UniqueConstraint(name = "uk_account_authority", columnNames = { "account_id", "authority_id" }) })
@Embeddable
public class AccountAuthority {
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_account_authority"))
    @Column(name = "authority_id", nullable = false)
    private Long authorityId;

    public AccountAuthority(Long authorityId) {
        this.authorityId = authorityId;
    }
}

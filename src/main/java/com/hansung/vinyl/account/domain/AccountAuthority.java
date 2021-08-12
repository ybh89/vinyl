package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.common.exception.validate.NullException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.AccessType.*;
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
        validate(authorityId);
        this.authorityId = authorityId;
    }

    private void validate(Long authorityId) {
        if (Objects.isNull(authorityId)) {
            throw new NullException("authorityId", getClass().getName());
        }
    }
}

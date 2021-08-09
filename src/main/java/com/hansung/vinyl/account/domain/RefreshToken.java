package com.hansung.vinyl.account.domain;

import com.google.common.base.Strings;
import com.hansung.vinyl.common.exception.validate.BlankException;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Access(AccessType.FIELD)
@Embeddable
public class RefreshToken {
    @Column(length = 150)
    private String refreshToken;

    public RefreshToken(String refreshToken) {
        validate(refreshToken);
        this.refreshToken = refreshToken;
    }

    private void validate(String refreshToken) {
        if (Strings.isNullOrEmpty(refreshToken) || refreshToken.isBlank()) {
            throw new BlankException("refreshToken", refreshToken, getClass().getName());
        }
    }

    public String value() {
        return refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refreshToken);
    }
}

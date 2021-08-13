package com.hansung.vinyl.account.domain;

import com.google.common.base.Strings;
import com.hansung.vinyl.common.exception.validate.BlankException;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
@Access(AccessType.FIELD)
@Embeddable
public class EncryptedPassword {
    @Column(nullable = false, length = 150)
    private String encryptedPassword;

    public EncryptedPassword(String encryptedPassword) {
        validate(encryptedPassword);
        this.encryptedPassword = encryptedPassword;
    }

    private void validate(String password) {
        if (Strings.isNullOrEmpty(password) || password.isBlank()) {
            throw new BlankException("password", password, getClass().getName());
        }
    }

    public String value() {
        return encryptedPassword;
    }
}

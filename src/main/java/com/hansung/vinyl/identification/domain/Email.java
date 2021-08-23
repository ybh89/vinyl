package com.hansung.vinyl.identification.domain;

import com.hansung.vinyl.common.exception.validate.FormatException;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PROTECTED;

@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
@Access(AccessType.FIELD)
@Embeddable
public class Email {
    private static final Pattern pattern = Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$");

    @Column(nullable = false, updatable = false, length = 50)
    private String email;

    public Email(String email) {
        validate(email);
        this.email = email;
    }

    public static Email of(String email) {
        return new Email(email);
    }

    private void validate(String email) {
        if (!pattern.matcher(email).matches()) {
            throw new FormatException("email", email, getClass().getName());
        }
    }

    public String value() {
        return email;
    }
}

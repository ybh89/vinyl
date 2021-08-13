package com.hansung.vinyl.member.domain;

import com.google.common.base.Strings;
import com.hansung.vinyl.common.exception.validate.BlankException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.util.Objects;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.*;

@EqualsAndHashCode
@Access(FIELD)
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Name {
    @Column(nullable = false, updatable = false, length = 50)
    private String name;

    public Name(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (Strings.isNullOrEmpty(name) || name.isBlank()) {
            throw new BlankException("name", name, getClass().getName());
        }
    }

    public String value() {
        return name;
    }
}

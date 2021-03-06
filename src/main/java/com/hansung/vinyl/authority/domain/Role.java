package com.hansung.vinyl.authority.domain;

import com.hansung.vinyl.common.exception.validate.FormatException;
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
public class Role {
    private static final String ROLE_PREFIX = "ROLE_";

    @Column(nullable = false, length = 50)
    private String role;

    public Role(String role) {
        validate(role);
        this.role = role;
    }

    private void validate(String role) {
        if (!role.startsWith(ROLE_PREFIX)) {
            throw new FormatException("role", role, getClass().getName());
        }
    }

    public String value() {
        return role;
    }

    public static Role of(String role) {
        return new Role(role);
    }
}

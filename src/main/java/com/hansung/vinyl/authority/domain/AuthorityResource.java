package com.hansung.vinyl.authority.domain;

import com.hansung.vinyl.common.exception.validate.NullException;
import com.hansung.vinyl.common.exception.validate.OutOfBoundsException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Objects;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Access(FIELD)
@Table(uniqueConstraints={ @UniqueConstraint(name = "uk_authority_resource", columnNames = { "authority_id", "path", "http_method" }) })
@Embeddable
public class AuthorityResource {
    @Embedded
    private Resource resource;

    public AuthorityResource(Resource resource) {
        validate(resource);
        this.resource = resource;
    }

    private void validate(Resource resource) {
        if (Objects.isNull(resource)) {
            throw new NullException("resource", getClass().getName());
        }
    }
}

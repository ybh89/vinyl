package com.hansung.vinyl.authority.domain;

import com.hansung.vinyl.common.exception.validate.NullException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PROTECTED;

@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Access(FIELD)
@Embeddable
public class Resource {
    @Embedded
    private Path path;

    @Enumerated(EnumType.STRING)
    @Column(name = "http_method", nullable = false, length = 10)
    private HttpMethod httpMethod;

    public Resource(String path, HttpMethod httpMethod) {
        validate(httpMethod);
        this.path = new Path(path);
        this.httpMethod = httpMethod;
    }

    private void validate(HttpMethod httpMethod) {
        if (Objects.isNull(httpMethod)) {
            throw new NullException("httpMethod", getClass().getName());
        }
    }

    public String getPathValue() {
        return path.value();
    }
}

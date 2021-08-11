package com.hansung.vinyl.authority.domain;

import com.hansung.vinyl.common.exception.validate.NullException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PROTECTED;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(path, resource.path) && httpMethod == resource.httpMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, httpMethod);
    }

    @Override
    public String toString() {
        return "Resource{" +
                "path='" + path + '\'' +
                ", httpMethod=" + httpMethod +
                '}';
    }

    public String getPathValue() {
        return path.value();
    }
}

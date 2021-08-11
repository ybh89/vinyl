package com.hansung.vinyl.authority.domain;

import com.google.common.base.Strings;
import com.hansung.vinyl.common.exception.validate.BlankException;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PROTECTED;

@Access(FIELD)
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Path {
    @Column(name = "path", nullable = false, length = 100)
    private String path;

    public Path(String path) {
        validate(path);
        this.path = path;
    }

    public void validate(String path) {
        if (Strings.isNullOrEmpty(path) || path.isBlank()) {
            throw new BlankException("path", path, getClass().getName());
        }
    }

    public String value() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path1 = (Path) o;
        return Objects.equals(path, path1.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}

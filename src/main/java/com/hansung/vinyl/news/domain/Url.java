package com.hansung.vinyl.news.domain;

import com.hansung.vinyl.common.exception.validate.FormatException;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PROTECTED;

@Access(FIELD)
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Url {
    private static final Pattern pattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/?([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");

    @Column
    private String sourceUrl;

    public Url(String sourceUrl) {
        validate(sourceUrl);
        this.sourceUrl = sourceUrl;
    }

    private void validate(String sourceUrl) {
        if (!pattern.matcher(sourceUrl).matches()) {
            throw new FormatException("sourceUrl", sourceUrl, getClass().getName());
        }
    }

    public String value() {
        return sourceUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url1 = (Url) o;
        return Objects.equals(sourceUrl, url1.sourceUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceUrl);
    }
}

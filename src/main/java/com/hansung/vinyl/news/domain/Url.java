package com.hansung.vinyl.news.domain;

import com.hansung.vinyl.common.exception.validate.FormatException;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
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
}

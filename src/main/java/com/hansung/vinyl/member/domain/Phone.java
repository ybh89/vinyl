package com.hansung.vinyl.member.domain;

import com.hansung.vinyl.common.exception.validate.FormatException;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

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
public class Phone {
    private static final Pattern pattern = Pattern.compile("^01(?:0|1|[6-9])?(\\d{3}|\\d{4})?(\\d{4})$");

    @Column(length = 15)
    private String phone;

    public Phone(String phone) {
        validate(phone);
        this.phone = phone;
    }

    private void validate(String phone) {
        if (Objects.isNull(phone)) {
            return;
        }
        if (!pattern.matcher(phone).matches()) {
            throw new FormatException("phone", phone, getClass().getName());
        }
    }

    public String value() {
        return phone;
    }
}

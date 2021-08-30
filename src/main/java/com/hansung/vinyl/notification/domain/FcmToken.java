package com.hansung.vinyl.notification.domain;

import com.google.common.base.Strings;
import com.hansung.vinyl.common.domain.BaseDateTimeEntity;
import com.hansung.vinyl.common.exception.validate.BlankException;
import com.hansung.vinyl.common.exception.validate.NullException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FcmToken extends BaseDateTimeEntity implements Persistable<Long> {
    @Id
    private Long id;

    @Column(length = 150, unique = true, nullable = false)
    private String token;

    public FcmToken(Long accountId, String token) {
        validate(accountId, token);
        this.id = accountId;
        this.token = token;
    }

    private void validate(Long accountId, String token) {
        if (Objects.isNull(accountId)) {
            throw new NullException("accountId", getClass().getName());
        }

        if (Strings.isNullOrEmpty(token) || token.isBlank()) {
            throw new BlankException("fcmToken", token, getClass().getName());
        }
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(getCreatedAt());
    }
}

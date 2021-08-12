package com.hansung.vinyl.notification.domain;

import com.hansung.vinyl.common.domain.BaseDateTimeEntity;
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
        this.id = accountId;
        this.token = token;
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(getCreatedAt());
    }
}

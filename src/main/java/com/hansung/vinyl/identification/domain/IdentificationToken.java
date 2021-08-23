package com.hansung.vinyl.identification.domain;

import com.hansung.vinyl.common.domain.BaseDateTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.AccessType.*;
import static lombok.AccessLevel.*;

@ToString
@Getter
@Access(FIELD)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class IdentificationToken extends BaseDateTimeEntity {
    private static final long VALIDITY = 5;

    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    private UUID id;

    private LocalDateTime expirationData;

    @Embedded
    private Email email;

    private boolean approved;

    @Builder
    private IdentificationToken(LocalDateTime expirationData, Email email) {
        this.expirationData = expirationData;
        this.email = email;
    }

    public static IdentificationToken create(String email) {
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(VALIDITY);
        return IdentificationToken.builder()
                .email(new Email(email))
                .expirationData(expirationDate)
                .build();
    }

    public IdentificationToken validate() {
        if (isExpired()) {
            approved = false;
            return this;
        }

        approved = true;
        return this;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationData);
    }
}

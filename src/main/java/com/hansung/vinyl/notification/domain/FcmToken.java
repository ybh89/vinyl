package com.hansung.vinyl.notification.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FcmToken {
    @Id
    private Long id;

    @Column(length = 150, unique = true, nullable = false)
    private String token;

    public FcmToken(Long accountId, String token) {
        this.id = accountId;
        this.token = token;
    }
}

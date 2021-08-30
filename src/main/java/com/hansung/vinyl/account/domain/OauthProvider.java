package com.hansung.vinyl.account.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Access(FIELD)
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class OauthProvider {
    @Column
    private String provider;
    @Column
    private String providerId;

    public OauthProvider(String provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }
}

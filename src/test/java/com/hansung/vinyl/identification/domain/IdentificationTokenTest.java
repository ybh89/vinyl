package com.hansung.vinyl.identification.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IdentificationTokenTest {
    @DisplayName("토큰 만료 여부")
    @Test
    public void 토큰_만료여부_확인() throws Exception {
        // given
        IdentificationToken identificationToken = IdentificationToken.create("identification@test.com");

        // when
        boolean expired = identificationToken.isExpired();

        // then
        assertThat(expired).isFalse();
    }

    @DisplayName("토큰 검증")
    @Test
    public void 토큰_검증_확인() throws Exception {
        // given
        IdentificationToken identificationToken = IdentificationToken.create("identification@test.com");

        // when
        IdentificationToken validatedToken = identificationToken.validate();

        // then
        assertThat(validatedToken.isApproved()).isTrue();
    }
}

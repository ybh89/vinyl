package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.common.exception.validate.BlankException;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("리플레시 토큰 도메인 테스트")
public class RefreshTokenTest {
    @DisplayName("리플레시 토큰 생성 예외 - 공백 예외")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public void 리플레시토큰_생성_예외1(String refreshToken) throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new RefreshToken(refreshToken)).isInstanceOf(BlankException.class);
    }

    @DisplayName("리플레시 토큰 생성 예외 - Null 예외")
    @Test
    public void 리플레시토큰_생성_예외2() throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new RefreshToken(null)).isInstanceOf(BlankException.class);
    }

    @DisplayName("리플레시 토큰 생성 확인")
    @Test
    public void 리플레시토큰_생성_확인() throws Exception {
        // when
        RefreshToken refreshToken = new RefreshToken("asd12ewad!@#");

        // then
        assertThat(refreshToken).isEqualTo(new RefreshToken("asd12ewad!@#"));
    }
}

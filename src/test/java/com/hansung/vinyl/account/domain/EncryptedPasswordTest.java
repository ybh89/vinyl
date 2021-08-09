package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.common.exception.validate.BlankException;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("패스워드 도메인 테스트")
public class EncryptedPasswordTest {
    @DisplayName("패스워드 생성 예외 - 공백 예외")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public void 패스워드_생성_예외1(String password) throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new EncryptedPassword(password)).isInstanceOf(BlankException.class);
    }

    @DisplayName("패스워드 생성 예외 - Null 예외")
    @Test
    public void 패스워드_생성_예외2() throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new EncryptedPassword(null)).isInstanceOf(BlankException.class);
    }

    @DisplayName("패스워드 생성 확인")
    @Test
    public void 패스워드_생성_확인() throws Exception {
        // when
        EncryptedPassword encryptedPassword = new EncryptedPassword("asd12ewad!@#");

        // then
        assertThat(encryptedPassword).isEqualTo(new EncryptedPassword("asd12ewad!@#"));
    }
}

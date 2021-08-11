package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.common.exception.validate.FormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("이메일 도메인 테스트")
public class EmailTest {
    @DisplayName("이메일 생성 예외 - 이메일 형식 예외")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "123", "asd", "!@#", "asd@asd", "asd.com"})
    public void 이메일_생성_예외(String email) throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Email(email)).isInstanceOf(FormatException.class);
    }

    @DisplayName("이메일 생성 확인")
    @Test
    public void 이메일_생성_확인() throws Exception {
        // when
        Email email = new Email("asd@asd.com");

        // then
        assertThat(email).isEqualTo(new Email("asd@asd.com"));
    }
}

package com.hansung.vinyl.authority.domain;

import com.hansung.vinyl.common.exception.validate.FormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("롤 도메인 테스트")
public class RoleTest {
    @DisplayName("롤 생성 예외 - prefix 예외")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "role_test", "roleTest"})
    public void 롤_생성_예외(String role) throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Role(role)).isInstanceOf(FormatException.class);
    }

    @DisplayName("롤 생성 확인")
    @Test
    public void 롤_생성_확인() throws Exception {
        // when
        Role role = new Role("ROLE_TEST");

        // then
        assertThat(role).isEqualTo(new Role("ROLE_TEST"));
    }
}

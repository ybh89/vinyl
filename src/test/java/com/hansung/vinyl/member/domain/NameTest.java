package com.hansung.vinyl.member.domain;

import com.hansung.vinyl.common.exception.validate.BlankException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("회원 이름 도메인 테스트")
public class NameTest {
    @DisplayName("회원 이름 생성 예외 - 이름이 빈문자열인 경우")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public void 회원이름_생성_예외1(String name) throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Name(name)).isInstanceOf(BlankException.class);
    }

    @DisplayName("회원 이름 생성 예외 - 이름이 Null 인 경우")
    @Test
    public void 회원이름_생성_예외2() throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Name(null)).isInstanceOf(BlankException.class);
    }

    @DisplayName("회원 이름 생성 확인")
    @Test
    public void 회원이름_생성_확인() throws Exception {
        // when
        Name name = new Name("윤빙구");

        // then
        assertThat(name).isEqualTo(new Name("윤빙구"));
    }
}

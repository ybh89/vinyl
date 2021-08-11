package com.hansung.vinyl.authority.domain;

import com.hansung.vinyl.common.exception.validate.BlankException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("경로 도메인 테스트")
public class PathTest {
    @DisplayName("경로 생성")
    @ParameterizedTest
    @ValueSource(strings = {"/*", "/**", "/members/*", "/members/**", "/members/?e", "/images/*.png", "/login"})
    public void 경로_생성_확인(String sPath) throws Exception {
        // when
        Path path = new Path(sPath);

        // then
        assertThat(path).isEqualTo(new Path(sPath));
    }

    @DisplayName("경로 생성 예외 - 공백 예외")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public void 경로_생성_예외1(String path) throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Path(path)).isInstanceOf(BlankException.class);
    }

    @DisplayName("경로 생성 예외 - Null 예외")
    @Test
    public void 경로_생성_예외2() throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Path(null)).isInstanceOf(BlankException.class);
    }
}

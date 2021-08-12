package com.hansung.vinyl.member.domain;

import com.hansung.vinyl.common.exception.validate.FormatException;
import com.hansung.vinyl.common.exception.validate.NullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("구독 도메인 테스트")
public class SubscribeTest {
    @DisplayName("구독 생성 예외 - 소식 아이디가 null 인 경우")
    @Test
    public void 구독_생성_예외1() throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Subscribe(null)).isInstanceOf(NullException.class);
    }

    @DisplayName("구독 생성 예외 - 소식 아이디가 1보다 작은 경우")
    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    public void 구독_생성_예외2(Long id) throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Subscribe(id)).isInstanceOf(FormatException.class);
    }

    @DisplayName("구독 생성 확인")
    @Test
    public void 구독_생성_확인() throws Exception {
        // when
        Subscribe subscribe = new Subscribe(1L);

        // then
        assertThat(subscribe).isEqualTo(new Subscribe(1L));
    }
}

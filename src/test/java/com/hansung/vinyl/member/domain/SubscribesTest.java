package com.hansung.vinyl.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구독 목록 도메인 테스트")
public class SubscribesTest {
    @DisplayName("구독하기")
    @Test
    public void 구독하기_확인() throws Exception {
        // given
        Subscribes subscribes = new Subscribes();

        // when
        subscribes.subscribe(1L);
        subscribes.subscribe(2L);

        // then
        assertThat(subscribes.getSubscribes()).containsExactly(1L, 2L);
    }

    @DisplayName("구독 취소하기")
    @Test
    public void 구독_취소하기_확인() throws Exception {
        // given
        Subscribes subscribes = new Subscribes();
        subscribes.subscribe(1L);
        subscribes.subscribe(2L);

        // when
        subscribes.unsubscribe(1L);

        // then
        assertThat(subscribes.getSubscribes()).containsExactly(2L);
    }
}

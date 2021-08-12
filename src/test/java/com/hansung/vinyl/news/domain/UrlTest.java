package com.hansung.vinyl.news.domain;

import com.hansung.vinyl.common.exception.validate.FormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Url 도메인 테스트")
public class UrlTest {
    @DisplayName("유알엘 생성 확인")
    @ParameterizedTest
    @ValueSource(strings = {"https://www.naver.com/", "http://www.naver.com/", "http://www.naver.com", "http://www.naver.com/image.png"})
    public void 소스유알엘_생성_확인(String url) throws Exception {
        // when
        Url sourceUrl = new Url(url);
        
        // then
        assertThat(sourceUrl).isEqualTo(new Url(url));
    }

    @DisplayName("유알엘 생성 예외 - 형식 예외")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "www.naver.com", "naver.com", })
    public void 소스유알엘_생성_예외(String url) throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Url(url)).isInstanceOf(FormatException.class);
    }
}

package com.hansung.vinyl.news.domain;

import com.hansung.vinyl.common.exception.file.NotSupportedFileExtensionException;
import com.hansung.vinyl.common.exception.validate.BlankException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("이미지 도메인 테스트")
public class ImageTest {
    @DisplayName("이미지 생성 예외 - 이미지명 공백 예외")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public void 이미지_생성_예외1(String imageName) throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Image(imageName, "uploadName.png")).isInstanceOf(BlankException.class);
        assertThatThrownBy(() -> new Image("storeName.png", imageName)).isInstanceOf(BlankException.class);
    }

    @DisplayName("이미지 생성 예외 - 이미지명 Null 예외")
    @Test
    public void 이미지_생성_예외2() throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Image(null, "uploadName.png")).isInstanceOf(BlankException.class);
        assertThatThrownBy(() -> new Image("storeName.png", null)).isInstanceOf(BlankException.class);
    }

    @DisplayName("이미지 생성 예외 - 이미지 확장자 타입 오류")
    @ParameterizedTest
    @ValueSource(strings = {"test.avi", "test.zip", "test.txt"})
    public void 이미지_생성_예외3(String imageName) throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Image(imageName, "uploadName.png"))
                .isInstanceOf(NotSupportedFileExtensionException.class);
        assertThatThrownBy(() -> new Image("storeName.png", imageName))
                .isInstanceOf(NotSupportedFileExtensionException.class);
    }

    @DisplayName("이미지 생성 확인")
    @Test
    public void 이미지_생성_확인() throws Exception {
        // when
        Image image = new Image("test.png", "test.png");

        // then
        assertThat(image).isEqualTo(new Image("test.png", "test.png"));
    }
}

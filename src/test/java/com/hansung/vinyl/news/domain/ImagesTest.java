package com.hansung.vinyl.news.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("이미지 그룹 도메인 테스트")
public class ImagesTest {
    @DisplayName("이미지 추가 확인")
    @Test
    public void 이미지_추가_확인() throws Exception {
        // given
        Images images = new Images();
        Image image1 = new Image("test1.png", "test1.png");
        Image image2 = new Image("test2.jpg", "test2.jpg");
        Image image3 = new Image("test3.jpeg", "test3.jpeg");

        // when
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image1);
        images.add(image1);

        // then
        assertThat(images.value()).containsExactly(image1, image2, image3);
    }

    @DisplayName("메인 이미지 조회 확인")
    @Test
    public void 메인이미지_조회_확인() throws Exception {
        // given
        Images images = new Images();
        Image image1 = new Image("test1.png", "test1.png");
        Image image2 = new Image("test2.jpg", "test2.jpg");
        Image image3 = new Image("test3.jpeg", "test3.jpeg");
        images.add(image1);
        images.add(image2);
        images.add(image3);

        // when
        Image mainImage = images.getMainImage();

        // then
        assertThat(mainImage).isEqualTo(new Image("test1.png", "test1.png"));
    }
}

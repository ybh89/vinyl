package com.hansung.vinyl.news.domain;

import com.hansung.vinyl.common.exception.validate.BlankException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("게시글 도메인 테스트")
public class PostTest {
    @DisplayName("게시글 생성 예외")
    @Test
    public void 게시글_생성_예외() throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Post("", "content", "topic", null))
                .isInstanceOf(BlankException.class);
        assertThatThrownBy(() -> new Post(null, "content", "topic", null))
                .isInstanceOf(BlankException.class);
        assertThatThrownBy(() -> new Post("title", "", "topic", null))
                .isInstanceOf(BlankException.class);
        assertThatThrownBy(() -> new Post("title", null, "topic", null))
                .isInstanceOf(BlankException.class);
        assertThatThrownBy(() -> new Post("title", "content", "", null))
                .isInstanceOf(BlankException.class);
        assertThatThrownBy(() -> new Post("title", "content", null, null))
                .isInstanceOf(BlankException.class);
    }

    @DisplayName("게시글 구독수 올리기")
    @Test
    public void 게시글구독수_올리기_확인() throws Exception {
        //given
        Post post = Post.builder()
                .title("title")
                .content("content")
                .topic("topic")
                .build();

        //when
        post.plusSubscribeCount();

        //then
        assertThat(post.getSubscribeCount()).isEqualTo(1);
    }

    @DisplayName("게시글 구독수 내리기")
    @Test
    public void 게시글구독수_내리기_확인() throws Exception {
        //given
        Post post = Post.builder()
                .title("title")
                .content("content")
                .topic("topic")
                .build();
        post.plusSubscribeCount();
        post.plusSubscribeCount();
        post.plusSubscribeCount();

        //when
        post.minusSubscribeCount();

        //then
        assertThat(post.getSubscribeCount()).isEqualTo(2);
    }
}

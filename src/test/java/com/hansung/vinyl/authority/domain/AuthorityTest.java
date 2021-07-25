package com.hansung.vinyl.authority.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("권한 비즈니스 로직 테스트")
public class AuthorityTest {
    @DisplayName("권한 경로 목록 조회")
    @Test
    public void 권한경로목록_조회_확인() throws Exception {
        //given
        Authority authority = 권한_생성(1L, "test", "test", Arrays.asList(new Path("/*"), new Path("/**")));

        //when
        List<Path> paths = authority.getPaths();

        //then
        assertThat(paths).containsExactly(new Path("/*"), new Path("/**"));
    }

    @DisplayName("권한 변경")
    @Test
    public void 권한_변경_확인() throws Exception {
        //given
        Authority authority1 = 권한_생성(1L, "test1", "test1", Arrays.asList(new Path("/*")));
        Authority authority2 = 권한_생성(2L, "test2", "test2", Arrays.asList(new Path("/**")));

        //when
        authority1.update(authority2, new AnnotationConfigApplicationContext());

        //then
        assertThat(authority1.getName()).isEqualTo("test2");
        assertThat(authority1.getRemark()).isEqualTo("test2");
        assertThat(authority1.getPaths()).containsExactly(new Path("/**"));
    }

    private Authority 권한_생성(Long id, String name, String remark, List<Path> paths) {
        return Authority.builder()
                .id(id)
                .name(name)
                .remark(remark)
                .paths(paths)
                .build();
    }
}

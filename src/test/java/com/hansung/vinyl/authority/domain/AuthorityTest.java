package com.hansung.vinyl.authority.domain;

import com.hansung.vinyl.common.exception.validate.IllegalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.hansung.vinyl.authority.domain.HttpMethod.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("권한 비즈니스 로직 테스트")
public class AuthorityTest {
    @DisplayName("권한 경로 목록 조회")
    @Test
    public void 권한경로목록_조회_확인() throws Exception {
        // given
        Authority authority = 권한_생성("ROLE_TEST", "test",
                Arrays.asList(new Resource("/*", POST), new Resource("/*", GET)));

        // when
        List<Resource> resources = authority.getResources();
        System.out.println(resources);

        // then
        assertThat(resources).containsExactly(new Resource("/*", POST), new Resource("/*", GET));
    }

    @DisplayName("권한 변경")
    @Test
    public void 권한_변경_확인() throws Exception {
        // given
        Authority authority1 = 권한_생성("ROLE_TEST1", "test1", Arrays.asList(new Resource("/*", GET)));
        Authority authority2 = 권한_생성("ROLE_TEST2", "test2", Arrays.asList(new Resource("/**", GET)));

        // when
        authority1.update(authority2);

        // then
        assertThat(authority1.getRoleValue()).isEqualTo("ROLE_TEST2");
        assertThat(authority1.getRemark()).isEqualTo("test2");
        assertThat(authority1.getResources()).containsExactly(new Resource("/**", GET));
    }

    @DisplayName("권한 삭제 예외 - 디폴트 권한 삭제")
    @Test
    public void 권한_삭제_예외() throws Exception {
        // given
        Authority authority = 권한_생성(DefaultRole.ROLE_USER.name(), "test1", Arrays.asList(new Resource("/*", GET)));

        // when
        // then
        assertThatThrownBy(() -> authority.delete())
                .hasMessage("디폴트 권한입니다.")
                .isInstanceOf(IllegalException.class);
    }

    private Authority 권한_생성(String role, String remark, List<Resource> resources) {
        return Authority.create(role, remark, resources);
    }
}

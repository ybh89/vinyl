package com.hansung.vinyl.authority.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.hansung.vinyl.authority.domain.HttpMethod.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("권한 자원 도메인 테스트")
public class AuthorityResourcesTest {
    @DisplayName("자원 수정 확인")
    @Test
    public void 자원_수정_확인() throws Exception {
        // given
        Authority authority = buildAuthority(1L, "ROLE_TEST", Arrays.asList(new Resource("/test", GET)));
        AuthorityResources authorityResources = authority.getAuthorityResources();
        Authority updateAuthority = buildAuthority(2L, "ROLE_TEST2", Arrays.asList(new Resource("/test2", POST)));
        AuthorityResources updateAuthorityResources = updateAuthority.getAuthorityResources();

        // when
        authorityResources.change(updateAuthorityResources);

        // then
        assertThat(authorityResources.getResources()).containsExactly(new Resource("/test2", POST));
    }

    private Authority buildAuthority(long id, String role, List<Resource> resources) {
        return Authority.create(role, "", resources);
    }
}

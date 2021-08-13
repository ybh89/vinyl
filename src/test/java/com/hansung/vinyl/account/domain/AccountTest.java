package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.authority.domain.Authority;
import com.hansung.vinyl.authority.domain.HttpMethod;
import com.hansung.vinyl.authority.domain.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("계정 도메인 로직 테스트")
public class AccountTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "test-password123";

    @DisplayName("계정 생성 확인")
    @Test
    public void 계정_생성_확인() throws Exception {
        //given
        Authority authority1 = buildAuthority(1L, "ROLE_TEST1", "test",
                Arrays.asList(new Resource("/test", HttpMethod.GET)));
        Authority authority2 = buildAuthority(2L, "ROLE_TEST2", "test",
                Arrays.asList(new Resource("/test", HttpMethod.POST)));

        //when
        Account account = buildAccount(EMAIL, PASSWORD, authority1, authority2);

        //then
        assertThat(account.getEmail()).isEqualTo(new Email(EMAIL));
        assertThat(account.getAuthorityIds()).containsExactly(1L, 2L);
    }

    private Account buildAccount(String email, String password, Authority authority1, Authority authority2) {
        return Account.builder()
                .email(email)
                .encryptedPassword(password)
                .authorities(Arrays.asList(authority1, authority2))
                .build();
    }

    private Authority buildAuthority(Long id, String name, String remark, List<Resource> resources) {
        return Authority.builder()
                .id(id)
                .role(name)
                .remark(remark)
                .resources(resources)
                .build();
    }
}

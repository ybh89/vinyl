package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.authority.domain.Authority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("계정 비즈니스 로직 테스트")
public class AccountTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "test-password123";

    @DisplayName("계정 생성 - 권한 확인")
    @Test
    public void 계정_생성_권한확인() throws Exception {
        //given
        Authority authority = 권한_생성(1L, "ROLE_USER");

        //when
        Account account = Account.builder()
                .id(1L)
                .email(EMAIL)
                .password(PASSWORD)
                .authorities(Arrays.asList(authority))
                .build();

        //then
        assertThat(account.getAccountAuthorities().get(0).getAuthorityId()).isEqualTo(1L);
    }

    @DisplayName("계정 권한 변경")
    @Test
    public void 계정권한_변경_확인() throws Exception {
        //given
        Authority authority1 = 권한_생성(1L, "ROLE_USER1");
        Authority authority2 = 권한_생성(2L, "ROLE_USER2");

        Account account = 계정_생성(1L, EMAIL, PASSWORD, Arrays.asList(authority1));

        //when
        account.changeAuthorities(Arrays.asList(authority2));

        //then
        assertThat(account.getAccountAuthorities().get(0).getAuthorityId()).isEqualTo(2L);
    }

    private Account 계정_생성(Long id, String email, String password, List<Authority> authorities) {
        return Account.builder()
                .id(id)
                .email(email)
                .password(password)
                .authorities(authorities)
                .build();
    }

    private Authority 권한_생성(Long id, String name) {
        return Authority.builder()
                .id(id)
                .name(name)
                .build();
    }
}

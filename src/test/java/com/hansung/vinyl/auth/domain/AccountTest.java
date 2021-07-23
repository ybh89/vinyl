package com.hansung.vinyl.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("계정 비즈니스 테스트")
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
        assertThat(account.getAccountAuthorities().get(0).getAuthority()).isEqualTo(authority);
    }

    private Authority 권한_생성(Long id, String name) {
        return Authority.builder()
                .id(id)
                .name(name)
                .build();
    }

    private Account 계정_생성(Long id, String email, String password) {
        return Account.builder()
                .id(id)
                .email(email)
                .password(password)
                .build();
    }
}

package com.hansung.vinyl.account.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("계정 권한 그룹 도메인 테스트")
public class AccountAuthoritiesTest {
    @DisplayName("계정 권한 그룹 수정 확인")
    @Test
    public void 계정권한그룹_수정_확인() throws Exception {
        //given
        AccountAuthority accountAuthority1 = new AccountAuthority(1L);
        AccountAuthority accountAuthority2 = new AccountAuthority(2L);
        AccountAuthority accountAuthority3 = new AccountAuthority(3L);
        AccountAuthority accountAuthority4 = new AccountAuthority(4L);
        AccountAuthorities accountAuthorities = new AccountAuthorities(Arrays.asList(accountAuthority1, accountAuthority2));

        //when
        accountAuthorities.change(Arrays.asList(accountAuthority3, accountAuthority4));

        //then
        assertThat(accountAuthorities.getAccountAuthorities()).containsExactly(accountAuthority3, accountAuthority4);
    }

    @DisplayName("계정 권한 그룹 권한아이디 목록 확인")
    @Test
    public void 계정권한그룹_권한아이디목록_확인() throws Exception {
        // given
        AccountAuthority accountAuthority1 = new AccountAuthority(1L);
        AccountAuthority accountAuthority2 = new AccountAuthority(2L);
        AccountAuthority accountAuthority3 = new AccountAuthority(3L);
        AccountAuthorities accountAuthorities = new AccountAuthorities(Arrays.asList(accountAuthority1,
                accountAuthority2, accountAuthority3));

        // when
        List<Long> authorityIds = accountAuthorities.getAuthorityIds();

        // then
        assertThat(authorityIds).containsExactly(1L, 2L, 3L);
    }
}

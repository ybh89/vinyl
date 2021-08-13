package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.authority.domain.Authority;
import com.hansung.vinyl.authority.domain.HttpMethod;
import com.hansung.vinyl.authority.domain.Resource;
import com.hansung.vinyl.member.domain.Gender;
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
        Authority authority1 = buildAuthority("ROLE_TEST1", "test",
                Arrays.asList(new Resource("/test", HttpMethod.GET)));
        Authority authority2 = buildAuthority("ROLE_TEST2", "test",
                Arrays.asList(new Resource("/test", HttpMethod.POST)));

        //when
        Account account = buildAccount(EMAIL, PASSWORD, authority1, authority2);

        //then
        assertThat(account.getEmail()).isEqualTo(new Email(EMAIL));
        assertThat(account.getAuthorityIds()).hasSize(2);
    }

    private Account buildAccount(String email, String password, Authority authority1, Authority authority2) {
        AccountInfo accountInfo = AccountInfo.builder()
                .email(email)
                .encryptedPassword(password)
                .authorities(Arrays.asList(authority1, authority2))
                .build();
        MemberInfo memberInfo = MemberInfo.builder()
                .name("test")
                .fcmToken("test")
                .gender(Gender.MALE)
                .build();
        return Account.create(accountInfo, memberInfo);
    }

    private Authority buildAuthority(String role, String remark, List<Resource> resources) {
        return Authority.create(role, remark, resources);
    }
}

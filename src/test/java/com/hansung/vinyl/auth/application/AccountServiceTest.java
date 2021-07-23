package com.hansung.vinyl.auth.application;

import com.hansung.vinyl.auth.domain.Account;
import com.hansung.vinyl.auth.domain.AccountRepository;
import com.hansung.vinyl.auth.dto.AccountRequest;
import com.hansung.vinyl.auth.dto.AccountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    @DisplayName("회원가입")
    @Test
    public void 회원가입_확인() throws Exception {
        //given
        AccountRequest accountRequest = 회원가입_요청_생성("test@test.com", "test-password@123");
        given(accountRepository.existsByEmail(anyString())).willReturn(false);
        given(accountRepository.save(any(Account.class)))
                .willReturn(Account.builder()
                        .id(1L)
                        .email(accountRequest.getEmail())
                        .password(accountRequest.getPassword())
                        .build());
        given(passwordEncoder.encode(anyString())).willReturn(accountRequest.getPassword());

        //when
        AccountResponse accountResponse = accountService.join(accountRequest);

        //then
        assertThat(accountResponse.getId()).isNotNull();
    }

    @DisplayName("회원가입 예외 - 이메일이 존재하는 경우")
    @Test
    public void 이메일이존재하는경우_회원가입_예외() throws Exception {
        //given
        AccountRequest accountRequest = 회원가입_요청_생성("test@test.com", "test-password@123");
        given(accountRepository.existsByEmail(anyString())).willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> accountService.join(accountRequest))
                .hasMessage("해당 아이디로 가입된 계정이 이미 존재합니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    private AccountRequest 회원가입_요청_생성(String email, String password) {
        return AccountRequest.builder()
                .email(email)
                .password(password)
                .build();
    }
}

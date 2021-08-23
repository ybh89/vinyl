package com.hansung.vinyl.account.ui;

import com.hansung.vinyl.account.application.AccountService;
import com.hansung.vinyl.account.dto.AccountAuthorityRequest;
import com.hansung.vinyl.account.dto.JoinRequest;
import com.hansung.vinyl.account.dto.JoinResponse;
import com.hansung.vinyl.account.dto.VerifyEmailResponse;
import com.hansung.vinyl.common.ControllerTest;
import com.hansung.vinyl.common.UnsecuredWebMvcTest;
import com.hansung.vinyl.member.domain.Gender;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("계정 컨트롤러 테스트")
@UnsecuredWebMvcTest(AccountController.class)
public class AccountControllerTest extends ControllerTest {
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "test-password123";

    @MockBean
    private AccountService accountService;
    private AccountApiDocumentDefinition accountApiDocumentDefinition;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);
        accountApiDocumentDefinition = new AccountApiDocumentDefinition(docResultHandler);
    }

    @DisplayName("회원 가입")
    @Test
    public void account_create() throws Exception {
        // given
        JoinRequest joinRequest = buildJoinRequest(EMAIL, PASSWORD, "testName");
        JoinResponse joinResponse = buildJoinResponse(1L, EMAIL);
        when(accountService.join(any())).thenReturn(joinResponse);

        // when
        ResultActions resultActions = post("/v1/accounts", joinRequest, false);

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(notNullValue())))
                .andExpect(jsonPath("$.email", Matchers.is(notNullValue())));

        // api documentation
        documentApi(resultActions, accountApiDocumentDefinition.회원_가입_api_문서());
    }

    @DisplayName("회원 탈퇴")
    @WithMockUser
    @Test
    public void account_delete() throws Exception {
        // when
        ResultActions resultActions = delete("/v1/accounts", null);

        // then
        resultActions.andExpect(status().isNoContent());

        // api documentation
        documentApi(resultActions, accountApiDocumentDefinition.회원_탈퇴_api_문서());
    }

    @DisplayName("계정 권한 변경")
    @Test
    public void account_authority_update() throws Exception {
        //given
        AccountAuthorityRequest accountAuthorityRequest = new AccountAuthorityRequest(Arrays.asList(1L));

        //when
        ResultActions resultActions = put("/v1/accounts/{accountId}/authorities",
                1L, accountAuthorityRequest);

        //then
        resultActions.andExpect(status().isNoContent());

        // api documentation
        documentApi(resultActions, accountApiDocumentDefinition.계정_권한_변경_api_문서());
    }

    @DisplayName("이메일 중복 확인")
    @Test
    public void account_email_check() throws Exception {
        // given
        VerifyEmailResponse verifyEmailResponse = new VerifyEmailResponse("email-check@verify.com",
                false, "해당 이메일로 회원 가입이 가능합니다.");
        when(accountService.verifyEmail(any())).thenReturn(verifyEmailResponse);
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.set("email", "email-check@verify.com");

        // when
        ResultActions resultActions = get("/v1/accounts/verify-email", null, params, false);

        // then
        resultActions.andExpect(status().isOk());

        // api documentation
        documentApi(resultActions, accountApiDocumentDefinition.이메일_중복_체크_api_문서());
    }

    private JoinResponse buildJoinResponse(Long id, String email) {
        return JoinResponse.builder()
                .id(id)
                .email(email)
                .build();
    }

    private JoinRequest buildJoinRequest(String email, String password, String name) {
        return JoinRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .authorityIds(Arrays.asList(1L, 2L))
                .gender(Gender.FEMALE)
                .phone("010-1111-1111")
                .fcmToken("this is fcm token")
                .build();
    }
}

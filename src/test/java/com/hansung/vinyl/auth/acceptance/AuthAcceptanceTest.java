package com.hansung.vinyl.auth.acceptance;

import com.hansung.vinyl.AcceptanceTest;
import com.hansung.vinyl.auth.dto.AccountRequest;
import com.hansung.vinyl.auth.dto.AccountResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("인증인가 관리")
public class AuthAcceptanceTest extends AcceptanceTest {
    @DisplayName("인증인가를 관리한다.")
    @Test
    public void authManager() throws Exception {
        ExtractableResponse<Response> postResponse = 회원가입_요청("test@test.com", "test-password!12");
        회원가입_됨(postResponse);

        ExtractableResponse<Response> getResponse = 회원_조회_요청(postResponse);
        회원_조회됨(getResponse);

        ExtractableResponse<Response> getListResponse = 회원_목록_조회_요청();
        회원_목록_조회됨(getListResponse);
    }

    private void 회원_목록_조회됨(ExtractableResponse<Response> getListResponse) {
        assertHttpStatus(getListResponse, OK);
    }

    private ExtractableResponse<Response> 회원_목록_조회_요청() {
        return get("/accounts");
    }

    private void 회원_조회됨(ExtractableResponse<Response> getResponse) {
        assertHttpStatus(getResponse, OK);
        assertThat(getResponse.as(AccountResponse.class).getId()).isNotNull();
    }

    private ExtractableResponse<Response> 회원_조회_요청(ExtractableResponse<Response> postResponse) {
        return get(postResponse.header("Location"));
    }

    private void 회원가입_됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private ExtractableResponse<Response> 회원가입_요청(String email, String password) {
        AccountRequest accountRequest = AccountRequest.builder()
                .email(email)
                .password(password)
                .build();

        ExtractableResponse<Response> postResponse = post("/accounts", accountRequest);
        return postResponse;
    }
}

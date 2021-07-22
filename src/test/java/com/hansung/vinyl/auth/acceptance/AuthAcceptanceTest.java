package com.hansung.vinyl.auth.acceptance;

import com.hansung.vinyl.AcceptanceTest;
import com.hansung.vinyl.auth.dto.AccountRequest;
import com.hansung.vinyl.auth.dto.AccountResponse;
import com.hansung.vinyl.auth.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("인증인가 관리")
public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "test-password!12";

    @DisplayName("인증인가를 관리한다.")
    @Test
    public void authManager() throws Exception {
        ExtractableResponse<Response> postResponse = 회원가입_요청(EMAIL, PASSWORD);
        회원가입_됨(postResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        String 토큰 = 로그인됨(loginResponse);

        ExtractableResponse<Response> getResponse = 회원_조회_요청(postResponse, 토큰);
        회원_조회됨(getResponse);

        ExtractableResponse<Response> getListResponse = 회원_목록_조회_요청(토큰);
        회원_목록_조회됨(getListResponse);
    }

    private String 로그인됨(ExtractableResponse<Response> loginResponse) {
        assertHttpStatus(loginResponse, OK);
        String token = loginResponse.header("access-token");
        assertThat(token).isNotNull();
        return token;
    }

    private ExtractableResponse<Response> 로그인_요청(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        ExtractableResponse<Response> loginResponse = post("/login", loginRequest);
        return loginResponse;
    }

    private void 회원_목록_조회됨(ExtractableResponse<Response> getListResponse) {
        assertHttpStatus(getListResponse, OK);
    }

    private ExtractableResponse<Response> 회원_목록_조회_요청(String token) {
        return get("/accounts", token);
    }

    private void 회원_조회됨(ExtractableResponse<Response> getResponse) {
        assertHttpStatus(getResponse, OK);
        assertThat(getResponse.as(AccountResponse.class).getId()).isNotNull();
    }

    private ExtractableResponse<Response> 회원_조회_요청(ExtractableResponse<Response> postResponse, String token) {
        return get(postResponse.header("Location"), token);
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

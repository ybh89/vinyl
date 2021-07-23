package com.hansung.vinyl.auth.acceptance;

import com.hansung.vinyl.AcceptanceTest;
import com.hansung.vinyl.auth.dto.AccountRequest;
import com.hansung.vinyl.auth.dto.AccountResponse;
import com.hansung.vinyl.auth.dto.AuthorityRequest;
import com.hansung.vinyl.auth.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("인증인가 관리")
public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "test-password!12";

    @DisplayName("인증인가를 관리한다.")
    @Test
    public void accountManager() throws Exception {
        ExtractableResponse<Response> postResponse = 계정_생성_요청(EMAIL, PASSWORD);
        계정_생성됨(postResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        String 토큰 = 로그인됨(loginResponse);

        ExtractableResponse<Response> getResponse = 계정_조회_요청(postResponse, 토큰);
        계정_조회됨(getResponse);

        ExtractableResponse<Response> getListResponse = 계정_목록_조회_요청(토큰);
        계정_목록_조회됨(getListResponse);
    }

    @DisplayName("권한을 관리한다.")
    @Test
    public void authorityManager() throws Exception {
        ExtractableResponse<Response> postResponse = 권한_생성_요청("ROLE_TEST", null, Arrays.asList("/accounts/**"));
        권한_생성됨(postResponse);

        ExtractableResponse<Response> getListResponse = 권한_목록_조회_요청();
        권한_목록_조회됨(getListResponse);

        // 권한과 함꼐 계정신청요청
        // 계정 조회 요청
        // 권한 수정
        // 권한 삭제
    }

    private void 권한_목록_조회됨(ExtractableResponse<Response> getListResponse) {
        assertHttpStatus(getListResponse, OK);
    }

    private ExtractableResponse<Response> 권한_목록_조회_요청() {
        return get("/authorities");
    }

    private void 권한_생성됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private ExtractableResponse<Response> 권한_생성_요청(String name, String desc, List<String> paths) {
        AuthorityRequest authorityRequest = new AuthorityRequest(name, desc, paths);
        ExtractableResponse<Response> postResponse = post("/authorities", authorityRequest);
        return postResponse;
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

    private void 계정_목록_조회됨(ExtractableResponse<Response> getListResponse) {
        assertHttpStatus(getListResponse, OK);
    }

    private ExtractableResponse<Response> 계정_목록_조회_요청(String token) {
        return get("/accounts", token);
    }

    private void 계정_조회됨(ExtractableResponse<Response> getResponse) {
        assertHttpStatus(getResponse, OK);
        assertThat(getResponse.as(AccountResponse.class).getId()).isNotNull();
    }

    private ExtractableResponse<Response> 계정_조회_요청(ExtractableResponse<Response> postResponse, String token) {
        return get(postResponse.header("Location"), token);
    }

    private void 계정_생성됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private ExtractableResponse<Response> 계정_생성_요청(String email, String password) {
        AccountRequest accountRequest = new AccountRequest(email, password, Arrays.asList());

        ExtractableResponse<Response> postResponse = post("/accounts", accountRequest);
        return postResponse;
    }
}

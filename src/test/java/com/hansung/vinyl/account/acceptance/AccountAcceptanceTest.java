package com.hansung.vinyl.account.acceptance;

import com.hansung.vinyl.AcceptanceTest;
import com.hansung.vinyl.account.dto.AccountRequest;
import com.hansung.vinyl.account.dto.AccountResponse;
import com.hansung.vinyl.security.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

public class AccountAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "test-password!12";

    @DisplayName("계정을 관리한다.")
    @Test
    public void accountManager() throws Exception {
        ExtractableResponse<Response> postResponse = 계정_생성_요청(EMAIL, PASSWORD);
        계정_생성됨(postResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        String 토큰 = 로그인됨(loginResponse);

        // 권한이 있어야 진행 가능
        /*ExtractableResponse<Response> getResponse = 계정_조회_요청(postResponse, 토큰);
        계정_조회됨(getResponse);

        ExtractableResponse<Response> getListResponse = 계정_목록_조회_요청(토큰);
        계정_목록_조회됨(getListResponse);

        ExtractableResponse<Response> deleteResponse = 계정_삭제_요청(postResponse, 토큰);
        계정_삭제됨(deleteResponse);*/
    }

    public static ExtractableResponse<Response> 계정_등록_되어있음(String email, String password, List<Long> authorityIds) {
        ExtractableResponse<Response> response = 계정_생성_요청(email, password, authorityIds);
        계정_생성됨(response);
        return response;
    }

    public static String 로그인_되어있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        String token = 로그인됨(response);
        return token;
    }

    public static ExtractableResponse<Response> 계정_목록_조회됨(String token) {
        ExtractableResponse<Response> response = 계정_목록_조회_요청(token);
        계정_목록_조회됨(response);
        return response;
    }

    private static String 로그인됨(ExtractableResponse<Response> loginResponse) {
        assertHttpStatus(loginResponse, OK);
        String token = loginResponse.header("access-token");
        assertThat(token).isNotNull();
        return token;
    }

    private static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        ExtractableResponse<Response> loginResponse = post("/login", loginRequest);
        return loginResponse;
    }

    private static void 계정_목록_조회됨(ExtractableResponse<Response> getListResponse) {
        assertHttpStatus(getListResponse, OK);
    }

    public static ExtractableResponse<Response> 계정_목록_조회_요청(String token) {
        return get("/accounts", token);
    }

    private void 계정_조회됨(ExtractableResponse<Response> getResponse) {
        assertHttpStatus(getResponse, OK);
        assertThat(getResponse.as(AccountResponse.class).getId()).isNotNull();
    }

    private ExtractableResponse<Response> 계정_조회_요청(ExtractableResponse<Response> postResponse, String token) {
        return get(postResponse.header("Location"), token);
    }

    private static void 계정_생성됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private static ExtractableResponse<Response> 계정_생성_요청(String email, String password) {
        AccountRequest accountRequest = new AccountRequest(email, password, Arrays.asList());

        ExtractableResponse<Response> postResponse = post("/accounts", accountRequest);
        return postResponse;
    }

    private static ExtractableResponse<Response> 계정_생성_요청(String email, String password, List<Long> ids) {
        AccountRequest accountRequest = new AccountRequest(email, password, ids);

        ExtractableResponse<Response> postResponse = post("/accounts", accountRequest);
        return postResponse;
    }

    private void 계정_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertHttpStatus(deleteResponse, NO_CONTENT);
    }

    private ExtractableResponse<Response> 계정_삭제_요청(ExtractableResponse<Response> postResponse, String 토큰) {
        return delete(postResponse.header("Location"), 토큰);
    }
}

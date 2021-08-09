package com.hansung.vinyl.account.acceptance;

import com.hansung.vinyl.account.dto.JoinRequest;
import com.hansung.vinyl.common.AcceptanceTest;
import com.hansung.vinyl.member.domain.Gender;
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
    public static final String NAME = "test";
    public static final String FCM_TOKEN = "test-fcm-token";

    @DisplayName("계정을 관리한다.")
    @Test
    public void accountManager() throws Exception {
        /**
         * TODO
         * given
         * 유저 권한 생성 후 회원가입때 전달해야함.
         */

        ExtractableResponse<Response> joinResponse = 회원가입_요청(EMAIL, PASSWORD, NAME, FCM_TOKEN);
        회원가입됨(joinResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        String 토큰 = 로그인됨(loginResponse).get(0);

        ExtractableResponse<Response> deleteResponse = 회원탈퇴_요청(joinResponse, 토큰);
        회원탈퇴됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 계정_등록_되어있음(String email, String password, List<Long> authorityIds, String name) {
        ExtractableResponse<Response> response = 회원가입_요청(email, password, name, authorityIds);
        회원가입됨(response);
        return response;
    }

    public static List<String> 로그인_되어있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        List<String> tokens = 로그인됨(response);
        return tokens;
    }

    public static ExtractableResponse<Response> 계정_목록_조회됨(String token) {
        ExtractableResponse<Response> response = 계정_목록_조회_요청(token);
        계정_목록_조회됨(response);
        return response;
    }

    private static List<String> 로그인됨(ExtractableResponse<Response> loginResponse) {
        assertHttpStatus(loginResponse, OK);
        String accessToken = loginResponse.header("access-token");
        String refreshToken = loginResponse.cookie("refresh-token");
        assertThat(accessToken).isNotNull();
        return Arrays.asList(accessToken, refreshToken);
    }

    private static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        ExtractableResponse<Response> loginResponse = post("/login", loginRequest);
        return loginResponse;
    }

    private static void 계정_목록_조회됨(ExtractableResponse<Response> getListResponse) {
        assertHttpStatus(getListResponse, OK);
    }
    public static ExtractableResponse<Response> 계정_목록_조회_요청(String accessToken) {
        return get("/accounts", accessToken);
    }
    public static ExtractableResponse<Response> 계정_목록_조회_요청(String accessToken, String refreshToken) {
        return get("/accounts", accessToken, refreshToken);
    }

    private static void 회원가입됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private static ExtractableResponse<Response> 회원가입_요청(String email, String password, String name, String fcmToken) {
        JoinRequest joinRequest = JoinRequest.builder()
                .email(email)
                .password(password)
                .authorityIds(Arrays.asList())
                .name(name)
                .gender(Gender.FEMALE)
                .fcmToken(fcmToken)
                .build();

        ExtractableResponse<Response> postResponse = post("/accounts", joinRequest);
        return postResponse;
    }

    private static ExtractableResponse<Response> 회원가입_요청(String email, String password, String name, List<Long> ids) {
        JoinRequest joinRequest = JoinRequest.builder()
                .email(email)
                .password(password)
                .authorityIds(ids)
                .name(name)
                .gender(Gender.FEMALE)
                .build();

        ExtractableResponse<Response> postResponse = post("/accounts", joinRequest);
        return postResponse;
    }

    private void 회원탈퇴됨(ExtractableResponse<Response> deleteResponse) {
        assertHttpStatus(deleteResponse, NO_CONTENT);
    }

    private ExtractableResponse<Response> 회원탈퇴_요청(ExtractableResponse<Response> postResponse, String 토큰) {
        return delete(postResponse.header("Location"), 토큰);
    }
}

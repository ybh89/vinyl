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

import static com.hansung.vinyl.identification.acceptance.IdentificationAcceptanceTest.본인인증_되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@DisplayName("계정 인수 테스트")
public class AccountAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "test2@test.com";
    public static final String PASSWORD = "test2-password!12";
    public static final String NAME = "test2";
    public static final String FCM_TOKEN = "test2-fcm-token";

    @DisplayName("계정을 관리한다.")
    @Test
    public void accountManager() throws Exception {
        // given
        본인인증_되어있음(EMAIL, testToken);

        ExtractableResponse<Response> joinResponse = 회원가입_요청(EMAIL, PASSWORD, NAME, FCM_TOKEN);
        회원가입됨(joinResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        String 토큰 = 로그인됨(loginResponse).get(0);

        // ROLE_USER 에 회원 탈퇴 권한이 필요함. /*/accounts, DELETE
        /*ExtractableResponse<Response> deleteResponse = 회원탈퇴_요청(토큰);
        회원탈퇴됨(deleteResponse);*/
    }

    public static ExtractableResponse<Response> 회원가입_되어있음(String email, String password, String name, String testToken) {
        본인인증_되어있음(email, testToken);
        ExtractableResponse<Response> response = 회원가입_요청(email, password, name, FCM_TOKEN);
        회원가입됨(response);
        return response;
    }

    public static ExtractableResponse<Response> 회원가입_되어있음(String email, String password, String name, String fcmToken, String testToken) {
        본인인증_되어있음(email, testToken);
        ExtractableResponse<Response> response = 회원가입_요청(email, password, name, fcmToken);
        회원가입됨(response);
        return response;
    }

    public static List<String> 로그인_되어있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        List<String> tokens = 로그인됨(response);
        return tokens;
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

    private static void 회원가입됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private static ExtractableResponse<Response> 회원가입_요청(String email, String password, String name, String fcmToken) {
        JoinRequest joinRequest = JoinRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .gender(Gender.FEMALE)
                .fcmToken(fcmToken)
                .build();

        ExtractableResponse<Response> postResponse = post("/v1/accounts", joinRequest);
        return postResponse;
    }

    private void 회원탈퇴됨(ExtractableResponse<Response> deleteResponse) {
        assertHttpStatus(deleteResponse, NO_CONTENT);
    }

    private ExtractableResponse<Response> 회원탈퇴_요청(String 토큰) {
        return delete("/v1/accounts", 토큰);
    }
}

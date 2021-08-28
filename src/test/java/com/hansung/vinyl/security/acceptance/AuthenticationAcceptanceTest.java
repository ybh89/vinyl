package com.hansung.vinyl.security.acceptance;

import com.hansung.vinyl.common.AcceptanceTest;
import com.hansung.vinyl.security.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.hansung.vinyl.account.acceptance.AccountAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 테스트")
public class AuthenticationAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "akass191@gmail.com";
    private static final String PASSWORD = "thisispassword123!@#";
    private static final String NAME = "윤병현";
    private static final String FCM_TOKEN = "thisisfcmtoken";

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원가입_되어있음(EMAIL, PASSWORD, NAME, FCM_TOKEN, testToken);
    }

    @DisplayName("로그인 성공")
    @Test
    public void 로그인_성공() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> loginResponse = post("/login", loginRequest);

        // then
        assertHttpStatus(loginResponse, HttpStatus.OK);
        String accessToken = loginResponse.header("access-token");
        String refreshToken = loginResponse.cookie("refresh-token");
        assertThat(accessToken).isNotBlank();
        assertThat(refreshToken).isNotBlank();
    }

    @DisplayName("로그인 실패")
    @Test
    public void 로그인_실패() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest(EMAIL, "wrong-password");

        // when
        ExtractableResponse<Response> loginResponse = post("/login", loginRequest);

        // then
        assertHttpStatus(loginResponse, HttpStatus.UNAUTHORIZED);
    }
}

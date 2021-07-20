package com.hansung.vinyl.auth.acceptance;

import com.hansung.vinyl.AcceptanceTest;
import com.hansung.vinyl.auth.dto.AccountRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpStatus.CREATED;

@DisplayName("인증인가 관리")
public class AuthAcceptanceTest extends AcceptanceTest {
    @DisplayName("인증인가를 관리한다.")
    @Test
    public void authManager() throws Exception {
        ExtractableResponse<Response> postResponse = 회원가입_요청("test@test.com", "test-password!12");
        회원가입_됨(postResponse);
    }

    private void 회원가입_됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private ExtractableResponse<Response> 회원가입_요청(String email, String password) {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setEmail(email);
        accountRequest.setPassword(password);
        ExtractableResponse<Response> postResponse = post("/accounts", accountRequest);
        return postResponse;
    }
}

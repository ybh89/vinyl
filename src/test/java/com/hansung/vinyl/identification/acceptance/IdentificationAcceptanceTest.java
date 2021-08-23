package com.hansung.vinyl.identification.acceptance;

import com.hansung.vinyl.common.AcceptanceTest;
import com.hansung.vinyl.identification.dto.IdentificationRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("본인 이메일 인증 인수 테스트")
public class IdentificationAcceptanceTest extends AcceptanceTest {
    @DisplayName("본인 이메일 인증 성공 시나리오")
    @Test
    public void 본인이메일인증_성공시나리오_확인() throws Exception {
        // 본인 이메일 인증 요청
        IdentificationRequest identificationRequest = new IdentificationRequest("identification@test.com");
        ExtractableResponse<Response> postResponse = 본인인증_요청(identificationRequest);
        본인인증_요청됨(postResponse);

        // 본인 이메일 인증 토큰 조회
        ExtractableResponse<Response> tokenResponse = 본인인증_토큰_조회_요청("identification@test.com", testToken);
        String token = tokenResponse.as(String.class);

        // 본인 이메일 인증 검증
        ExtractableResponse<Response> validateResponse = 본인인증_검증_요청(token);
        본인인증_검증됨(validateResponse);

        // 본인 이메일 인증 결과 확인
        ExtractableResponse<Response> getResponse = 본인인증_결과_조회_요청("identification@test.com");
        본인인증_결과_조회됨(getResponse);
    }

    private void 본인인증_결과_조회됨(ExtractableResponse<Response> getResponse) {
        assertHttpStatus(getResponse, HttpStatus.OK);
    }

    private ExtractableResponse<Response> 본인인증_결과_조회_요청(String email) {
        return get("/v1/identifications?email=" + email);
    }

    private void 본인인증_검증됨(ExtractableResponse<Response> validateResponse) {
        assertHttpStatus(validateResponse, HttpStatus.OK);
    }

    private static ExtractableResponse<Response> 본인인증_검증_요청(String token) {
        return get("/v1/identifications/" + token);
    }

    private static ExtractableResponse<Response> 본인인증_토큰_조회_요청(String email, String testToken) {
        return get("/emails/token?email=" + email, testToken);
    }

    private void 본인인증_요청됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, HttpStatus.CREATED);
    }

    private static ExtractableResponse<Response> 본인인증_요청(IdentificationRequest identificationRequest) {
        return post("/v1/identifications", identificationRequest, null);
    }

    public static void 본인인증_되어있음(String email, String testToken) {
        IdentificationRequest identificationRequest = new IdentificationRequest(email);
        본인인증_요청(identificationRequest);
        ExtractableResponse<Response> tokenResponse = 본인인증_토큰_조회_요청(email, testToken);
        String token = tokenResponse.as(String.class);
        본인인증_검증_요청(token);
    }
}

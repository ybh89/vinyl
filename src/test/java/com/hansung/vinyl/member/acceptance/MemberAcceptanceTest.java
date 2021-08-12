package com.hansung.vinyl.member.acceptance;

import com.hansung.vinyl.common.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("회원 인수 테스트")
public class MemberAcceptanceTest extends AcceptanceTest {
    @DisplayName("회원을 관리한다")
    @Test
    public void memberManager() throws Exception {
        // 프로필 조회(user)
        ExtractableResponse<Response> profileResponse = 프로필_조회_요청();
        조회됨(profileResponse);

        // 구독 목록 조회(user)
        ExtractableResponse<Response> subscribesResponse = 구독_목록_조회_요청();
        조회됨(subscribesResponse);

        // 회원 조회(admin)
        ExtractableResponse<Response> memberResponse = 회원_조회_요청();
        조회됨(memberResponse);

        // 회원 목록 조회(admin)
        ExtractableResponse<Response> membersResponse = 회원_목록_조회_요청();
        조회됨(membersResponse);
    }

    private ExtractableResponse<Response> 회원_목록_조회_요청() {
        return get("/members", testToken);
    }

    private ExtractableResponse<Response> 회원_조회_요청() {
        return get("/members/1", testToken);
    }

    private ExtractableResponse<Response> 구독_목록_조회_요청() {
        return get("/members/subscribes", testToken);
    }

    private void 조회됨(ExtractableResponse<Response> profileResponse) {
        assertHttpStatus(profileResponse, HttpStatus.OK);
    }

    private ExtractableResponse<Response> 프로필_조회_요청() {
        return get("/members/me", testToken);
    }
}
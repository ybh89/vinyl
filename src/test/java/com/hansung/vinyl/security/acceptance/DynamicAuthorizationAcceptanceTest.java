package com.hansung.vinyl.security.acceptance;

import com.hansung.vinyl.authority.domain.HttpMethod;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.authority.dto.ResourceRequest;
import com.hansung.vinyl.common.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static com.hansung.vinyl.account.acceptance.AccountAcceptanceTest.로그인_되어있음;
import static com.hansung.vinyl.account.acceptance.AccountAcceptanceTest.회원가입_되어있음;
import static com.hansung.vinyl.authority.acceptance.AuthorityAcceptanceTest.*;
import static com.hansung.vinyl.news.acceptance.NewsAcceptanceTest.소식_조회됨;

@DisplayName("동적 인가 시스템 테스트")
public class DynamicAuthorizationAcceptanceTest extends AcceptanceTest {
    private static final String USER_EMAIL = "user@user.com";
    private static final String USER_PASSWORD = "user-password!123";
    private static final String USER_NAME = "user";
    private static final String USER_FCM_TOKEN = "user-fcm-token";

    @DisplayName("동적 인가 확인")
    @Test
    public void 동적_인가_확인() throws Exception {
        // 사용자 권한 생성
        ResourceRequest resourceRequest = new ResourceRequest("/*/members/**", HttpMethod.GET);
        ExtractableResponse<Response> authorityResponse = 권한_등록_되어있음("ROLE_USER", "test user role",
                Arrays.asList(resourceRequest), testToken);

        // 사용자 계정 생성
        ExtractableResponse<Response> userResponse = 회원가입_되어있음(USER_EMAIL, USER_PASSWORD,
                Arrays.asList(authorityResponse.as(AuthorityResponse.class).getId()), USER_NAME, USER_FCM_TOKEN);

        // 사용자 로그인
        String userToken = 로그인_되어있음(USER_EMAIL, USER_PASSWORD).get(0);

        // 사용자가 권한이 없는 자원에 접근 실패
        ExtractableResponse<Response> authorityListResponse = 권한_목록_조회_요청(userToken);
        권한_목록_조회_실패됨(authorityListResponse);

        // 사용자 권한에 자원 접근 권한 부여
        ResourceRequest updateResourceRequest1 = new ResourceRequest("/*/members/**", HttpMethod.GET);
        ResourceRequest updateResourceRequest2 = new ResourceRequest("/*/authorities", HttpMethod.GET);
        ExtractableResponse<Response> updateAuthorityResponse = 권한_수정_요청(authorityResponse, testToken,
                "ROLE_USER", "update test role user", Arrays.asList(updateResourceRequest1,
                        updateResourceRequest2));
        assertHttpStatus(updateAuthorityResponse, HttpStatus.OK);

        // 사용자가 자원에 다시 접근 성공
        ExtractableResponse<Response> authorityListResponse2 = 권한_목록_조회_요청(userToken);
        권한_목록_조회됨(authorityListResponse2);

        // 사용자 권한에 자원 접근 권한 삭제
        ExtractableResponse<Response> updateAuthorityResponse2 = 권한_수정_요청(authorityResponse, testToken,
                "ROLE_USER", "update test role user2", Arrays.asList(updateResourceRequest1));
        assertHttpStatus(updateAuthorityResponse2, HttpStatus.OK);

        // 사용자가 자원에 접근 실패
        ExtractableResponse<Response> authorityListResponse3 = 권한_목록_조회_요청(userToken);
        권한_목록_조회_실패됨(authorityListResponse3);

        // 매니저 권한 생성
        ResourceRequest mgrResourceRequest = new ResourceRequest("/*/authorities/**", HttpMethod.GET);
        ExtractableResponse<Response> mgrAuthorityResponse = 권한_등록_되어있음("ROLE_MANAGER", "test manager role",
                Arrays.asList(mgrResourceRequest), testToken);

        // 사용자를 매니저 권한으로 변경
        계정_권한_변경됨(userResponse, Arrays.asList(mgrAuthorityResponse.as(AuthorityResponse.class).getId()), testToken);

        // 사용자가 자원에 접근 성공
        ExtractableResponse<Response> authorityListResponse4 = 권한_목록_조회_요청(userToken);
        권한_목록_조회됨(authorityListResponse4);

        // 매니저 권한 삭제
        ExtractableResponse<Response> deleteResponse = delete(mgrAuthorityResponse.header("Location"), testToken);
        assertHttpStatus(deleteResponse, HttpStatus.NO_CONTENT);

        // 사용자가 자원에 접근 실패
        ExtractableResponse<Response> authorityListResponse5 = 권한_목록_조회_요청(userToken);
        권한_목록_조회_실패됨(authorityListResponse5);
    }

    @DisplayName("익명 사용자 인가 확인")
    @Test
    public void 익명사용자_인가_확인() throws Exception {
        // 소식 목록 조회 가능
        소식_조회됨();

        // 프로필 조회 불가능
        ExtractableResponse<Response> response = 인증없이_프로필_조회_요청();
        프로필_조회_실패됨(response);
    }

    private void 프로필_조회_실패됨(ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.UNAUTHORIZED);
    }

    private ExtractableResponse<Response> 인증없이_프로필_조회_요청() {
        return get("v1/members/me");
    }

    private void 권한_목록_조회됨(ExtractableResponse<Response> authorityListResponse2) {
        assertHttpStatus(authorityListResponse2, HttpStatus.OK);
    }

    private void 권한_목록_조회_실패됨(ExtractableResponse<Response> authorityListResponse) {
        assertHttpStatus(authorityListResponse, HttpStatus.FORBIDDEN);
    }
}

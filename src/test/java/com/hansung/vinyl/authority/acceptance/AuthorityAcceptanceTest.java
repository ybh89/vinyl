package com.hansung.vinyl.authority.acceptance;

import com.hansung.vinyl.account.dto.AccountAuthorityRequest;
import com.hansung.vinyl.authority.domain.*;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.authority.dto.ResourceRequest;
import com.hansung.vinyl.common.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.hansung.vinyl.account.acceptance.AccountAcceptanceTest.*;
import static org.springframework.http.HttpStatus.*;

@DisplayName("권한 인수 테스트")
public class AuthorityAcceptanceTest extends AcceptanceTest {
    @DisplayName("권한을 관리한다.")
    @Test
    public void authorityManager() throws Exception {
        // 권한 생성
        ResourceRequest resourceRequest = new ResourceRequest("/*/test/**", HttpMethod.GET);
        ExtractableResponse<Response> postAuthorityResponse = 권한_생성_요청("ROLE_CREATE", null,
                Arrays.asList(resourceRequest), testToken);
        권한_생성됨(postAuthorityResponse);

        // 권한 조회
        ExtractableResponse<Response> getListResponse = 권한_목록_조회_요청(testToken);
        권한_목록_조회됨(getListResponse);

        // 권한 수정
        ResourceRequest updateResourceRequest = new ResourceRequest("/*/test/*", HttpMethod.POST);
        ExtractableResponse<Response> putResponse = 권한_수정_요청(postAuthorityResponse, testToken, "ROLE_UPDATE",
                "UPDATE-TEST", Arrays.asList(updateResourceRequest));
        권한_수정됨(putResponse);

        // 계정 권한 변경
        ResourceRequest newResourceRequest = new ResourceRequest("/*/new/**", HttpMethod.POST);
        ExtractableResponse<Response> newAuthorityResponse = 권한_생성_요청("ROLE_NEW", "NEW",
                Arrays.asList(newResourceRequest), testToken);
        권한_생성됨(newAuthorityResponse);
        ExtractableResponse<Response> response = 회원가입_되어있음("new@new.com", "new-password123",
                Arrays.asList(postAuthorityResponse.as(AuthorityResponse.class).getId()), "new-name", testToken);

        ExtractableResponse<Response> changeResponse = 계정_권한_변경_요청(response,
                Arrays.asList(newAuthorityResponse.as(AuthorityResponse.class).getId()), testToken);
        계정_권한_변경됨(changeResponse);

        // 권한 삭제
        ExtractableResponse<Response> deleteResponse = 권한_삭제_요청(postAuthorityResponse, testToken);
        권한_삭제됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 권한_수정_되어있음(ExtractableResponse<Response> postResponse, String token,
                                                           String name, String desc, List<ResourceRequest> resourceRequests) {
        ExtractableResponse<Response> response = 권한_수정_요청(postResponse, token, name, desc, resourceRequests);
        권한_수정됨(response);
        return response;
    }

    public static ExtractableResponse<Response> 권한_등록_되어있음(String name, String desc,
                                                           List<ResourceRequest> resourceRequests, String token) {
        ExtractableResponse<Response> response = 권한_생성_요청(name, desc, resourceRequests, token);
        권한_생성됨(response);
        return response;
    }

    public static ExtractableResponse<Response> 계정_권한_변경됨(ExtractableResponse<Response> accountPostResponse,
                                                          List<Long> authorityIds, String 토큰) {
        ExtractableResponse<Response> response = 계정_권한_변경_요청(accountPostResponse, authorityIds, 토큰);
        계정_권한_변경됨(response);
        return response;
    }

    private static void 계정_권한_변경됨(ExtractableResponse<Response> changeResponse) {
        assertHttpStatus(changeResponse, NO_CONTENT);
    }

    private static ExtractableResponse<Response> 계정_권한_변경_요청(ExtractableResponse<Response> accountPostResponse,
                                                             List<Long> authorityIds, String 토큰) {
        AccountAuthorityRequest accountAuthorityRequest = new AccountAuthorityRequest(authorityIds);
        ExtractableResponse<Response> changeResponse = put(accountPostResponse.header("Location")
                        + "/authorities", accountAuthorityRequest, 토큰);
        return changeResponse;
    }

    private void 권한_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertHttpStatus(deleteResponse, NO_CONTENT);
    }

    private ExtractableResponse<Response> 권한_삭제_요청(ExtractableResponse<Response> postResponse, String 토큰) {
        return delete(postResponse.header("Location"), 토큰);
    }

    private static void 권한_수정됨(ExtractableResponse<Response> putResponse) {
        assertHttpStatus(putResponse, OK);
    }

    public static ExtractableResponse<Response> 권한_수정_요청(ExtractableResponse<Response> postResponse, String token,
                                                         String name, String desc, List<ResourceRequest> resourceRequests) {
        AuthorityRequest authorityRequest = new AuthorityRequest(name, desc, resourceRequests);
        ExtractableResponse<Response> putResponse = put(postResponse.header("Location"), authorityRequest, token);
        return putResponse;
    }

    private static void 권한_목록_조회됨(ExtractableResponse<Response> getListResponse) {
        assertHttpStatus(getListResponse, OK);
    }

    public static ExtractableResponse<Response> 권한_목록_조회_요청(String token) {
        return get("/v1/authorities", token);
    }

    private static void 권한_생성됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private static ExtractableResponse<Response> 권한_생성_요청(String name, String desc,
                                                          List<ResourceRequest> resourceRequests, String token) {
        AuthorityRequest authorityRequest = new AuthorityRequest(name, desc, resourceRequests);
        ExtractableResponse<Response> postResponse = post("/v1/authorities", authorityRequest, token);
        return postResponse;
    }
}

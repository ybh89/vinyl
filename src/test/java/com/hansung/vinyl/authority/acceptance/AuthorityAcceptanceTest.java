package com.hansung.vinyl.authority.acceptance;

import com.hansung.vinyl.AcceptanceTest;
import com.hansung.vinyl.account.dto.AccountAuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.authority.dto.ResourceRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.hansung.vinyl.account.acceptance.AccountAcceptanceTest.*;
import static com.hansung.vinyl.account.acceptance.AccountAcceptanceTest.계정_등록_되어있음;
import static org.springframework.http.HttpStatus.*;

@DisplayName("인증인가 관리")
public class AuthorityAcceptanceTest extends AcceptanceTest {
    @DisplayName("권한을 관리한다.")
    @Test
    public void authorityManager() throws Exception {
        /*//권한 생성
        ExtractableResponse<Response> postResponse = 권한_생성_요청("ROLE_TEST", null, Arrays.asList("/accounts/**"));
        권한_생성됨(postResponse);

        // 권한과 함꼐 계정신청요청
        ExtractableResponse<Response> accountPostResponse = 계정_등록_되어있음(EMAIL, PASSWORD,
                Arrays.asList(postResponse.as(AuthorityResponse.class).getId()));

        // 로그인
        String 토큰 = 로그인_되어있음(EMAIL, PASSWORD);

        // 권한 조회
        ExtractableResponse<Response> getListResponse = 권한_목록_조회_요청(토큰);
        권한_목록_조회됨(getListResponse);

        // 권한 수정
        ExtractableResponse<Response> putResponse = 권한_수정_요청(postResponse, 토큰, "ROLE_UPDATE",
                "UPDATE-TEST", Arrays.asList("/**"));
        권한_수정됨(putResponse);

        // 계정 권한 변경
        ExtractableResponse<Response> newResponse = 권한_생성_요청("ROLE_NEW", "NEW", Arrays.asList("/accounts/**"));
        권한_생성됨(newResponse);
        ExtractableResponse<Response> changeResponse = 계정_권한_변경_요청(accountPostResponse,
                Arrays.asList(newResponse.as(AuthorityResponse.class).getId()), 토큰);
        계정_권한_변경됨(changeResponse);

        // 권한 삭제
        ExtractableResponse<Response> deleteResponse = 권한_삭제_요청(postResponse, 토큰);
        권한_삭제됨(deleteResponse);*/
    }

    public static ExtractableResponse<Response> 권한_등록_되어있음(String name, String desc,
                                                           List<ResourceRequest> resourceRequests, String token) {
        ExtractableResponse<Response> response = 권한_생성_요청(name, desc, resourceRequests, token);
        권한_생성됨(response);
        return response;
    }

    public static ExtractableResponse<Response> 권한_목록_조회됨(String token) {
        ExtractableResponse<Response> response = 권한_목록_조회_요청(token);
        권한_목록_조회됨(response);
        return response;
    }

    public static ExtractableResponse<Response> 계정_권한_변경됨(ExtractableResponse<Response> accountPostResponse,
                                                          List<Long> authorityIds, String 토큰) {
        ExtractableResponse<Response> response = 계정_권한_변경_요청(accountPostResponse, authorityIds, 토큰);
        계정_권한_변경됨(response);
        return response;
    }

    private static void 계정_권한_변경됨(ExtractableResponse<Response> changeResponse) {
        assertHttpStatus(changeResponse, OK);
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

    private void 권한_수정됨(ExtractableResponse<Response> putResponse) {
        assertHttpStatus(putResponse, OK);
    }

    private ExtractableResponse<Response> 권한_수정_요청(ExtractableResponse<Response> postResponse, String token,
                                                   String name, String desc, List<ResourceRequest> resourceRequests) {
        AuthorityRequest authorityRequest = new AuthorityRequest(name, desc, resourceRequests);
        ExtractableResponse<Response> putResponse = put(postResponse.header("Location"), authorityRequest, token);
        return putResponse;
    }

    private static void 권한_목록_조회됨(ExtractableResponse<Response> getListResponse) {
        assertHttpStatus(getListResponse, OK);
    }

    public static ExtractableResponse<Response> 권한_목록_조회_요청(String token) {
        return get("/authorities", token);
    }

    private static void 권한_생성됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private static ExtractableResponse<Response> 권한_생성_요청(String name, String desc,
                                                          List<ResourceRequest> resourceRequests, String token) {
        AuthorityRequest authorityRequest = new AuthorityRequest(name, desc, resourceRequests);
        ExtractableResponse<Response> postResponse = post("/authorities", authorityRequest, token);
        return postResponse;
    }
}

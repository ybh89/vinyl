package com.hansung.vinyl.authority.acceptance;

import com.hansung.vinyl.AcceptanceTest;
import com.hansung.vinyl.account.application.AccountService;
import com.hansung.vinyl.account.dto.JoinRequest;
import com.hansung.vinyl.authority.application.AuthorityService;
import com.hansung.vinyl.authority.domain.HttpMethod;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.authority.dto.ResourceRequest;
import com.hansung.vinyl.member.domain.Gender;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hansung.vinyl.account.acceptance.AccountAcceptanceTest.*;
import static com.hansung.vinyl.authority.acceptance.AuthorityAcceptanceTest.*;
import static com.hansung.vinyl.authority.domain.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@DisplayName("동적 인가 시스템 관리")
public class DynamicAuthorityAcceptanceTest extends AcceptanceTest {
    private static final String ADMIN_EMAIL = "admin@admin.com";
    private static final String ADMIN_PASSWORD = "admin-password!123";
    private static final String MGR_EMAIL = "mgr@mgr.com";
    private static final String MGR_PASSWORD = "mgr-password!123";
    private static final String MGR_NAME = "mgr";
    private static final String USER_EMAIL = "user@user.com";
    private static final String USER_PASSWORD = "user-password!123";
    private static final String USER_NAME = "user";

    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthorityService authorityService;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // 어플리케이션은 초기에 관리자 계정, 관리자 권한 데이터를 가지고 시작한다.
        setUpAdmin();
    }

    @DisplayName("동적 인가 시스템을 관리한다")
    @Test
    public void dynamicAuthorityManager() throws Exception {
        String 관리자_토큰 = 로그인_되어있음(ADMIN_EMAIL, ADMIN_PASSWORD).get(0);

        List<ResourceRequest> 매니저권한자원 = createResourceRequestWithAllHttpMethod("/accounts/**");
        List<ResourceRequest> 사용자권한자원 = createResourceRequestWithAllHttpMethod("/authorities/**");

        ExtractableResponse<Response> 매니저권한 = 권한_등록_되어있음("ROLE_MANAGER", "", 매니저권한자원, 관리자_토큰);
        ExtractableResponse<Response> 사용자권한 = 권한_등록_되어있음("ROLE_USER", "", 사용자권한자원, 관리자_토큰);

        ExtractableResponse<Response> 매니저 = 계정_등록_되어있음(MGR_EMAIL, MGR_PASSWORD, null, MGR_NAME);
        ExtractableResponse<Response> 사용자 = 계정_등록_되어있음(USER_EMAIL, USER_PASSWORD, null, USER_NAME);

        계정_권한_변경됨(매니저, Arrays.asList(매니저권한.as(AuthorityResponse.class).getId()), 관리자_토큰);
        계정_권한_변경됨(사용자, Arrays.asList(사용자권한.as(AuthorityResponse.class).getId()), 관리자_토큰);

        String 매니저_토큰 = 로그인_되어있음(MGR_EMAIL, MGR_PASSWORD).get(0);

        //매니저 권한 테스트
        ExtractableResponse<Response> response = 권한_목록_조회_요청(매니저_토큰);
        권한_목록_조회_실패됨(response);
        계정_목록_조회됨(매니저_토큰);

        //매니저 권한 변경 테스트
        ResourceRequest 권한_목록_조회_권한 = new ResourceRequest("/authorities", GET);
        매니저권한자원.add(권한_목록_조회_권한);
        권한_수정_되어있음(매니저권한, 관리자_토큰, "ROLE_MANAGER", "updated", 매니저권한자원);
        ExtractableResponse<Response> response2 = 권한_목록_조회_요청(매니저_토큰);
        권한_목록_조회됨(response2);

        //사용자 권한으로 변경
        계정_권한_변경됨(매니저, Arrays.asList(사용자권한.as(AuthorityResponse.class).getId()), 관리자_토큰);
        ExtractableResponse<Response> accountsResponse = 계정_목록_조회_요청(매니저_토큰);
        계정_목록_조회_실패됨(accountsResponse);
    }

    private void 계정_목록_조회_실패됨(ExtractableResponse<Response> accountsResponse) {
        assertHttpStatus(accountsResponse, FORBIDDEN);
    }

    private void 권한_목록_조회됨(ExtractableResponse<Response> response2) {
        assertHttpStatus(response2, OK);
    }

    private void 권한_목록_조회_실패됨(ExtractableResponse<Response> response) {
        assertHttpStatus(response, FORBIDDEN);
    }

    private List<ResourceRequest> createResourceRequestWithAllHttpMethod(String path) {
        List<ResourceRequest> resourceRequests = new ArrayList<>();
        for (HttpMethod httpMethod : HttpMethod.values()) {
            resourceRequests.add(new ResourceRequest(path, httpMethod));
        }
        return resourceRequests;
    }

    public void setUpAdmin() {
        List<ResourceRequest> 관리자권한자원 = createResourceRequestWithAllHttpMethod("/**");
        AuthorityRequest authorityRequest = new AuthorityRequest("ROLE_ADMIN", "test", 관리자권한자원);
        AuthorityResponse authorityResponse = authorityService.create(authorityRequest);
        JoinRequest joinRequest = new JoinRequest(ADMIN_EMAIL, ADMIN_PASSWORD, Arrays.asList(authorityResponse.getId()),
                "super", "", Gender.FEMALE);
        accountService.join(joinRequest);
    }
}

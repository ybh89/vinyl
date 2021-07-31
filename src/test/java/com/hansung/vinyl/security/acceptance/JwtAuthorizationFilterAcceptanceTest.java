package com.hansung.vinyl.security.acceptance;

import com.hansung.vinyl.AcceptanceTest;
import com.hansung.vinyl.account.application.AccountService;
import com.hansung.vinyl.account.dto.JoinRequest;
import com.hansung.vinyl.authority.application.AuthorityService;
import com.hansung.vinyl.authority.domain.HttpMethod;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.authority.dto.ResourceRequest;
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

@DisplayName("JWT 검증 테스트")
public class JwtAuthorizationFilterAcceptanceTest extends AcceptanceTest {
    private static final String EXPIRED_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiYXV0aG9yaXRpZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2Mjc0ODY0MTIsImV4cCI6MTYyNzQ4NjQxM30.dtlYXym6rqa0wZETAcp3d0jVckyc0tTvSgtcUzalV-Q";
    private static final String ADMIN_EMAIL = "admin@admin.com";
    private static final String ADMIN_PASSWORD = "admin-password!123";

    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthorityService authorityService;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // 어플리케이션은 초기에 관리자 계정, 관리자 권한 데이터를 가지고 시작한다.
        setUpAdmin();
        setUpUser();
    }

    @DisplayName("JWT 를 검증한다.")
    @Test
    public void JWTManager() throws Exception {
        List<String> tokens = 로그인_되어있음(EMAIL, PASSWORD);
        String accessToken = tokens.get(0);
        String refreshToken = tokens.get(1);

        계정_목록_조회됨(accessToken);

        // 시간이 흘러 access token 이 만료됨.
        ExtractableResponse<Response> response = 계정_목록_조회_요청(EXPIRED_ACCESS_TOKEN, refreshToken);
        //assertHttpStatus(response, HttpStatus.UNAUTHORIZED);
    }

    private void setUpAdmin() {
        List<ResourceRequest> 관리자권한자원 = createResourceRequestWithAllHttpMethod("/**");
        AuthorityRequest authorityRequest = new AuthorityRequest("ROLE_ADMIN", "test", 관리자권한자원);
        AuthorityResponse authorityResponse = authorityService.create(authorityRequest);
        JoinRequest joinRequest = new JoinRequest(ADMIN_EMAIL, ADMIN_PASSWORD, Arrays.asList(authorityResponse.getId()));
        accountService.join(joinRequest);
    }

    private void setUpUser() {
        ResourceRequest resourceRequest = new ResourceRequest("/accounts", HttpMethod.GET);
        AuthorityRequest authorityRequest = new AuthorityRequest("ROLE_USER", "test", Arrays.asList(resourceRequest));
        AuthorityResponse authorityResponse = authorityService.create(authorityRequest);
        계정_등록_되어있음(EMAIL, PASSWORD, Arrays.asList(authorityResponse.getId()));
    }

    private List<ResourceRequest> createResourceRequestWithAllHttpMethod(String path) {
        List<ResourceRequest> resourceRequests = new ArrayList<>();
        for (HttpMethod httpMethod : HttpMethod.values()) {
            resourceRequests.add(new ResourceRequest(path, httpMethod));
        }
        return resourceRequests;
    }

    /**
     * 1. 사용자가 ID , PW를 통해 로그인합니다.
     * 2. 서버에서는 회원 DB에서 값을 비교합니다(보통 PW는 일반적으로 암호화해서 들어갑니다)
     * 3~4. 로그인이 완료되면 Access Token, Refresh Token을 발급합니다. 이때 일반적으로 회원DB에 Refresh Token을 저장해둡니다.
     * 5. 사용자는 Refresh Token은 안전한 저장소에 저장 후, Access Token을 헤더에 실어 요청을 보냅니다.
     * 6~7. Access Token을 검증하여 이에 맞는 데이터를 보냅니다.
     * 8. 시간이 지나 Access Token이 만료됐다고 보겠습니다.
     * 9. 사용자는 이전과 동일하게 Access Token을 헤더에 실어 요청을 보냅니다.
     * 10~11. 서버는 Access Token이 만료됨을 확인하고 권한없음을 신호로 보냅니다.
     * ** Access Token 만료가 될 때마다 계속 과정 9~11을 거칠 필요는 없습니다.
     *  사용자(프론트엔드)에서 Access Token의 Payload를 통해 유효기간을 알 수 있습니다. 따라서 프론트엔드 단에서 API 요청 전에 토큰이 만료됐다면 바로 재발급 요청을 할 수도 있습니다.
     * 12. 사용자는 Refresh Token과 Access Token을 함께 서버로 보냅니다.
     * 13. 서버는 받은 Access Token이 조작되지 않았는지 확인한후, Refresh Token과 사용자의 DB에 저장되어 있던 Refresh Token을 비교합니다. Token이 동일하고 유효기간도 지나지 않았다면 새로운 Access Token을 발급해줍니다.
     * 14. 서버는 새로운 Access Token을 헤더에 실어 다시 API 요청을 진행합니다.
     */
}

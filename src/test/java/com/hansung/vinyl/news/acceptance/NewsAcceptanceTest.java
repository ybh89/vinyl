package com.hansung.vinyl.news.acceptance;

import com.hansung.vinyl.common.AcceptanceTest;
import com.hansung.vinyl.account.application.AccountService;
import com.hansung.vinyl.account.dto.JoinRequest;
import com.hansung.vinyl.account.dto.JoinResponse;
import com.hansung.vinyl.authority.application.AuthorityService;
import com.hansung.vinyl.authority.domain.HttpMethod;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.authority.dto.ResourceRequest;
import com.hansung.vinyl.member.domain.Gender;
import com.hansung.vinyl.news.dto.NewsRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.*;

@DisplayName("소식 관리")
public class NewsAcceptanceTest extends AcceptanceTest {
    private static final String ADMIN_EMAIL = "admin@admin.com";
    private static final String ADMIN_PASSWORD = "admin-password!123";

    private Long adminId;

    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthorityService authorityService;

    @BeforeEach
    public void setUp() {
        super.setUp();
        setUpAdmin();
    }

    @DisplayName("소식을 관리한다")
    @Test
    public void newsManager() throws Exception {
        /*String 관리자_토큰 = 로그인_되어있음(ADMIN_EMAIL, ADMIN_PASSWORD).get(0);
        MultipartFile multipartFile = new MockMultipartFile("testImage.png", "testImage.png",
                "image/png", new FileInputStream("/Users/ybh/Downloads/testImage.png"));
        NewsRequest newsRequest = NewsRequest.builder()
                .title("testTitle")
                .content("testContent")
                .brand("testBrand")
                .price("140")
                .priceType(PriceType.USD)
                .topic("testTopic")
                .images(Arrays.asList(multipartFile))
                .build();
        // when
        Map<String, String> params = new HashMap<>();
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().multiPart()
                .then().log().all().extract();
        
        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus..value());
        ExtractableResponse<Response> response = 소식_생성_요청(관리자_토큰, newsRequest);
        소식_생성됨(response);*/
    }

    private void 소식_생성됨(ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.CREATED);
    }

    private ExtractableResponse<Response> 소식_생성_요청(String 관리자_토큰, NewsRequest newsRequest) {
        return post("/news", newsRequest, 관리자_토큰);
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
        JoinRequest joinRequest = JoinRequest.builder()
                .email(ADMIN_EMAIL)
                .password(ADMIN_PASSWORD)
                .authorityIds(Arrays.asList(authorityResponse.getId()))
                .name("super")
                .gender(Gender.FEMALE)
                .build();
        JoinResponse join = accountService.join(joinRequest);
        adminId = join.getId();
    }
}

package com.hansung.vinyl.news.acceptance;

import com.hansung.vinyl.AcceptanceTest;
import com.hansung.vinyl.account.application.AccountService;
import com.hansung.vinyl.account.dto.JoinRequest;
import com.hansung.vinyl.account.dto.JoinResponse;
import com.hansung.vinyl.authority.application.AuthorityService;
import com.hansung.vinyl.authority.domain.HttpMethod;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.authority.dto.ResourceRequest;
import com.hansung.vinyl.member.domain.Gender;
import com.hansung.vinyl.news.domain.PriceType;
import com.hansung.vinyl.news.dto.NewsRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hansung.vinyl.account.acceptance.AccountAcceptanceTest.로그인_되어있음;

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
        String 관리자_토큰 = 로그인_되어있음(ADMIN_EMAIL, ADMIN_PASSWORD).get(0);
        MultipartFile multipartFile = new MockMultipartFile("testImage.png", "testImage.png",
                "png", Files.readAllBytes(Paths.get("/Users/ybh/Downloads/testImage.png")));
        NewsRequest newsRequest = new NewsRequest(adminId, "testTitle", "testContent",
                "https://www.naver.com/", "140", PriceType.USD, Arrays.asList(multipartFile));
        ExtractableResponse<Response> response = 소식_생성_요청(관리자_토큰, newsRequest);
        소식_생성됨(response);
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
        JoinRequest joinRequest = new JoinRequest(ADMIN_EMAIL, ADMIN_PASSWORD, Arrays.asList(authorityResponse.getId()),
                "super", "", Gender.FEMALE);
        JoinResponse join = accountService.join(joinRequest);
        adminId = join.getId();
    }
}

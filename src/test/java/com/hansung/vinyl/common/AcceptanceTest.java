package com.hansung.vinyl.common;

import com.hansung.vinyl.authority.domain.*;
import com.hansung.vinyl.security.infrastructure.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.*;

import static com.hansung.vinyl.account.acceptance.AccountAcceptanceTest.로그인_되어있음;
import static com.hansung.vinyl.account.acceptance.AccountAcceptanceTest.회원가입_되어있음;
import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    public static final String TEST_EMAIL = "test@test.com";
    public static final String TEST_PASSWORD = "test-password123";
    public static final String TEST_NAME = "test-name";

    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private UrlFilterInvocationSecurityMetadataSource metadataSource;
    @LocalServerPort
    int port;
    @Autowired
    private DatabaseCleanup databaseCleanup;
    protected String testToken;
    protected Authority testAuthority;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        Authority authority = createAuthority();
        testToken = setTestAccount(authority);
        testAuthority = authority;
    }

    public static ExtractableResponse<Response> post(String path, Object params, String token) {
        token = setBlankIfNull(token);
        return given()
                        .log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(token)
                .when()
                        .post(path)
                .then()
                        .log().all()
                        .extract();
    }

    public static ExtractableResponse<Response> post(String path, Object params) {
        return post(path, params, null);
    }

    public static ExtractableResponse<Response> put(String path, Object params, String token) {
        token = setBlankIfNull(token);
        return given()
                        .log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(token)
                .when()
                        .put(path)
                .then()
                        .log().all()
                        .extract();
    }

    public static ExtractableResponse<Response> put(String path, Object params) {
        return put(path, params, null);
    }

    public static ExtractableResponse<Response> get(String path, String accessToken, String refreshToken) {
        return given()
                        .log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(accessToken)
                        .cookie("refresh-token", refreshToken)
                .when()
                        .get(path)
                .then()
                        .log().all()
                        .extract();
    }

    public static ExtractableResponse<Response> get(String path, String token) {
        token = setBlankIfNull(token);
        return given()
                        .log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(token)
                .when()
                        .get(path)
                .then()
                        .log().all()
                        .extract();
    }

    public static ExtractableResponse<Response> get(String path) {
        return get(path, null);
    }

    public static ExtractableResponse<Response> delete(String path, String token) {
        token = setBlankIfNull(token);
        return given()
                        .log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(token)
                .when()
                        .delete(path)
                .then()
                        .log().all()
                        .extract();
    }

    public static ExtractableResponse<Response> delete(String path) {
        return delete(path, null);
    }

    public static ExtractableResponse<Response> postWithMultipart(String path, File file, String controlName,
                                                                  Map<String, String> params, String token) {
        return given()
                        .log().all()
                        .auth().oauth2(token)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .multiPart(controlName, file)
                        .formParams(params)
                .when()
                        .post(path)
                .then()
                        .log().all()
                        .extract();
        }

    public static ExtractableResponse<Response> putWithMultipart(String path, File file, String controlName,
                                                                  Map<String, String> params, String token) {
        return given()
                        .log().all()
                        .auth().oauth2(token)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .multiPart(controlName, file)
                        .formParams(params)
                .when()
                        .put(path)
                .then()
                        .log().all()
                        .extract();
    }

    public static void assertHttpStatus(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    private static String setBlankIfNull(String token) {
        if (Objects.isNull(token)) {
            token = "";
        }
        return token;
    }

    public String setTestAccount(Authority authority) {
        회원가입_되어있음(TEST_EMAIL, TEST_PASSWORD, Arrays.asList(authority.getId()), TEST_NAME);
        return 로그인_되어있음(TEST_EMAIL, TEST_PASSWORD).get(0);
    }

    protected Authority createAuthority() {
        Authority testAuthority = buildAuthority("ROLE_TEST", "/**");
        Authority saveAuthority = authorityRepository.save(testAuthority);
        metadataSource.reload(new AuthorityCommandedEvent());
        return saveAuthority;
    }

    protected static List<Resource> createPermitAllResource(String path) {
        List<Resource> resources = new ArrayList<>();
        for (HttpMethod value : HttpMethod.values()) {
            resources.add(new Resource(path, value));
        }
        return resources;
    }

    private Authority buildAuthority(String role, String path) {
        return Authority.builder()
                .role(role)
                .resources(createPermitAllResource(path))
                .build();
    }
}

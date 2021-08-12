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
import com.hansung.vinyl.news.domain.PriceType;
import com.hansung.vinyl.news.dto.NewsRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@DisplayName("소식 인수 테스트")
public class NewsAcceptanceTest extends AcceptanceTest {
    private static final String TEST_IMAGE_PATH = "src/test/resources/static/testImage.png";
    private static final String TEST_IMAGE_PATH2 = "src/test/resources/static/testImage2.png";

    @DisplayName("소식을 관리한다")
    @Test
    public void newsManager() throws Exception {
        // 소식 생성
        Map<String, String> params = 소식_생성_파라미터();
        File file = new File(TEST_IMAGE_PATH);
        ExtractableResponse<Response> postResponse = 소식_생성_요청(params, file);
        소식_생성됨(postResponse);

        // 소식 조회
        ExtractableResponse<Response> newsResponse = 소식_조회_요청(postResponse);
        소식_조회됨(newsResponse);

        // 소식 목록 조회
        ExtractableResponse<Response> newsListResponse = 소식_목록_조회_요청();
        소식_목록_조회됨(newsListResponse);

        // 소식 수정
        Map<String, String> updateParams = 소식_수정_파라미터();
        File updateFile = new File(TEST_IMAGE_PATH2);
        ExtractableResponse<Response> updateResponse = 소식_수정_요청(postResponse, updateParams, updateFile);
        소식_수정됨(updateResponse);

        // 소식 삭제
        ExtractableResponse<Response> deleteResponse = 소식_삭제_요청(postResponse);
        소식_삭제됨(deleteResponse);
    }

    private void 소식_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertHttpStatus(deleteResponse, HttpStatus.NO_CONTENT);
    }

    private ExtractableResponse<Response> 소식_삭제_요청(ExtractableResponse<Response> postResponse) {
        return delete(postResponse.header("Location"), testToken);
    }

    private void 소식_수정됨(ExtractableResponse<Response> updateResponse) {
        assertHttpStatus(updateResponse, HttpStatus.OK);
    }

    private ExtractableResponse<Response> 소식_수정_요청(ExtractableResponse<Response> postResponse, Map<String, String> updateParams, File updateFile) {
        return putWithMultipart(postResponse.header("Location"),
                updateFile, "images", updateParams, testToken);
    }

    private Map<String, String> 소식_수정_파라미터() {
        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("title", "this is title2");
        updateParams.put("content", "this is content2");
        updateParams.put("topic", "topic2");
        updateParams.put("catalogName", "IU LP2");
        updateParams.put("brand", "SM2");
        updateParams.put("price", "1402");
        updateParams.put("priceType", "USD");
        updateParams.put("releaseDate", "2022-12-12T12:12:12");
        updateParams.put("sourceUrl", "https://www.naver.com/");
        return updateParams;
    }

    private void 소식_목록_조회됨(ExtractableResponse<Response> newsListResponse) {
        assertHttpStatus(newsListResponse, HttpStatus.OK);
    }

    private ExtractableResponse<Response> 소식_목록_조회_요청() {
        return get("/news", testToken);
    }

    private void 소식_조회됨(ExtractableResponse<Response> newsResponse) {
        assertHttpStatus(newsResponse, HttpStatus.OK);
    }

    private ExtractableResponse<Response> 소식_조회_요청(ExtractableResponse<Response> postResponse) {
        return get(postResponse.header("Location"), testToken);
    }

    private void 소식_생성됨(ExtractableResponse<Response> response1) {
        assertHttpStatus(response1, HttpStatus.CREATED);
    }

    private ExtractableResponse<Response> 소식_생성_요청(Map<String, String> params, File file) {
        return postWithMultipart("/news", file, "images", params, testToken);
    }

    private Map<String, String> 소식_생성_파라미터() {
        Map<String, String> params = new HashMap<>();
        params.put("title", "this is title");
        params.put("content", "this is content");
        params.put("topic", "topic");
        params.put("catalogName", "IU LP");
        params.put("brand", "SM");
        params.put("price", "140");
        params.put("priceType", "USD");
        params.put("releaseDate", "2022-12-12T12:12:12");
        params.put("sourceUrl", "https://www.naver.com/");
        return params;
    }
}

package com.hansung.vinyl.news.ui;

import com.hansung.vinyl.common.ControllerTest;
import com.hansung.vinyl.common.UnsecuredWebMvcTest;
import com.hansung.vinyl.news.application.NewsService;
import com.hansung.vinyl.news.domain.PriceType;
import com.hansung.vinyl.news.dto.ImageResponse;
import com.hansung.vinyl.news.dto.NewsListResponse;
import com.hansung.vinyl.news.dto.NewsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("소식 컨트롤러 테스트")
@UnsecuredWebMvcTest(NewsController.class)
public class NewsControllerTest extends ControllerTest {
    private static final String TEST_IMAGE_PATH = "src/test/resources/static/testImage.png";

    private NewsApiDocumentDefinition newsApiDocumentDefinition;
    @MockBean
    private NewsService newsService;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);
        newsApiDocumentDefinition = new NewsApiDocumentDefinition(docResultHandler);
    }

    @DisplayName("소식 생성")
    @Test
    public void news_create() throws Exception {
        // given
        MockMultipartFile file = getMockMultipartFile();
        MultiValueMap<String, String> params = buildParams();
        NewsResponse newsResponse = buildNewsResponse();
        when(newsService.create(any())).thenReturn(newsResponse);

        // when
        ResultActions resultActions = postWithMultipart("/news", null, params, file);

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())));

        // api documentation
        documentApi(resultActions, newsApiDocumentDefinition.소식_생성_api_문서());
    }

    @DisplayName("소식 수정")
    @WithMockUser
    @Test
    public void news_update() throws Exception {
        // given
        MockMultipartFile file = getMockMultipartFile();
        MultiValueMap<String, String> params = buildParams();
        NewsResponse newsResponse = buildNewsResponse();
        when(newsService.update(any(), any(), any())).thenReturn(newsResponse);

        // when
        ResultActions resultActions = putWithMultipart("/news/{newsId}", 1L, params, file);

        // then
        resultActions.andExpect(status().isOk());

        // api documentation
        documentApi(resultActions, newsApiDocumentDefinition.소식_수정_api_문서());
    }

    @DisplayName("소식 목록 조회")
    @Test
    public void news_list() throws Exception {
        // given
        NewsListResponse newsListResponse = buildNewsListResponse();
        MultiValueMap<String, String> params = buildQueryParams();
        when(newsService.list(any(Pageable.class))).thenReturn(new SliceImpl<>(Arrays.asList(newsListResponse)));

        // when
        ResultActions resultActions = get("/news", null, params, false);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$..['id']").exists());

        // api documentation
        documentApi(resultActions, newsApiDocumentDefinition.소식_목록_조회_api_문서());
    }

    @DisplayName("소식 상세 조회")
    @Test
    public void news_detail() throws Exception {
        //given
        NewsResponse newsResponse = buildNewsResponse();
        when(newsService.find(any())).thenReturn(newsResponse);

        //when
        ResultActions resultActions = get("/news/{newsId}", 1L, null, false);

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        // api documentation
        documentApi(resultActions, newsApiDocumentDefinition.소식_상세_조회_api_문서());
    }

    @DisplayName("소식 삭제")
    @WithMockUser
    @Test
    public void news_delete() throws Exception {
        //when
        ResultActions resultActions = delete("/news/{newsId}", 1L);

        //then
        resultActions.andExpect(status().isNoContent());

        // api documentation
        documentApi(resultActions, newsApiDocumentDefinition.소식_삭제_api_문서());
    }

    private NewsListResponse buildNewsListResponse() {
        return NewsListResponse.builder()
                .id(1L)
                .title("test title")
                .price("140")
                .priceType(PriceType.USD)
                .releaseDate(LocalDateTime.parse("2022-12-12T12:12:12",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                .topic("test topic")
                .mainThumbnailImage(new byte[]{})
                .build();
    }

    private MultiValueMap<String, String> buildQueryParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.set("page", "0");
        params.set("size", "3");
        params.set("sort", "id,desc");
        return params;
    }

    private MockMultipartFile getMockMultipartFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(TEST_IMAGE_PATH);
        return new MockMultipartFile("images", fileInputStream);
    }

    private NewsResponse buildNewsResponse() {
        ImageResponse imageResponse = ImageResponse.builder()
                .storeOriginalImageName("original-store-image.png")
                .storeThumbnailImageName("thumbnail-store-image.png")
                .uploadName("testImage.png")
                .build();

        NewsResponse newsResponse = NewsResponse.builder()
                .id(1L)
                .title("test title")
                .content("test content")
                .brand("test brand")
                .price("140")
                .priceType(PriceType.USD)
                .sourceUrl("https://yoonbing9.tistory.com/38?category=979979")
                .releaseDate(LocalDateTime.parse("2022-12-12T12:12:12",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                .topic("test topic")
                .mainThumbnailImage(new byte[]{})
                .images(Arrays.asList(imageResponse))
                .build();
        return newsResponse;
    }

    private MultiValueMap<String, String> buildParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.set("title", "test title");
        params.set("content", "test content");
        params.set("brand", "test brand");
        params.set("price", "140");
        params.set("priceType", "USD");
        params.set("sourceUrl", "https://yoonbing9.tistory.com/38?category=979979");
        params.set("releaseDate", "2022-12-12T12:12:12");
        params.set("topic", "test topic");
        return params;
    }
}

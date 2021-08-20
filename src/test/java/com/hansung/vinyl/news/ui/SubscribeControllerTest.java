package com.hansung.vinyl.news.ui;

import com.hansung.vinyl.common.ControllerTest;
import com.hansung.vinyl.common.UnsecuredWebMvcTest;
import com.hansung.vinyl.news.application.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("구독 컨트롤러 테스트")
@UnsecuredWebMvcTest(SubscribeController.class)
public class SubscribeControllerTest extends ControllerTest {
    private SubscribeApiDocumentDefinition subscribeApiDocumentDefinition;
    @MockBean
    private NewsService newsService;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);
        subscribeApiDocumentDefinition = new SubscribeApiDocumentDefinition(docResultHandler);
    }

    @DisplayName("구독하기")
    @WithMockUser
    @Test
    public void subscribe() throws Exception {
        // when
        ResultActions resultActions = put("/v1/subscribes/{newsId}", 1L, null);

        // then
        resultActions.andExpect(status().isNoContent());

        // api documentation
        documentApi(resultActions, subscribeApiDocumentDefinition.소식_구독_api_문서());
    }

    @DisplayName("구독 취소하기")
    @WithMockUser
    @Test
    public void unsubscribe() throws Exception {
        // when
        ResultActions resultActions = delete("/v1/subscribes/{newsId}", 1L);

        // then
        resultActions.andExpect(status().isNoContent());

        // api documentation
        documentApi(resultActions, subscribeApiDocumentDefinition.소식_구독취소_api_문서());
    }
}

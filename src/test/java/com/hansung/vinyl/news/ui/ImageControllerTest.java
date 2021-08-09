package com.hansung.vinyl.news.ui;

import com.hansung.vinyl.common.ControllerTest;
import com.hansung.vinyl.common.UnsecuredWebMvcTest;
import com.hansung.vinyl.news.domain.service.ImageStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UnsecuredWebMvcTest(ImageController.class)
public class ImageControllerTest extends ControllerTest {
    private ImageApiDocumentDefinition imageApiDocumentDefinition;
    @MockBean
    private ImageStore imageStore;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);
        imageApiDocumentDefinition = new ImageApiDocumentDefinition(docResultHandler);
    }

    @DisplayName("이미지 조회")
    @Test
    public void image() throws Exception {
        // given
        String imageFullPath = "src/test/resources/static/testImage.png";
        when(imageStore.getFullPath(any())).thenReturn(imageFullPath);

        // when
        ResultActions resultActions = get("/images/{imageName}", "testImage.png", null, false);

        // then
        resultActions.andExpect(status().isOk());

        // api documentation
        documentApi(resultActions, imageApiDocumentDefinition.이미지_조회_api_문서());
    }
}

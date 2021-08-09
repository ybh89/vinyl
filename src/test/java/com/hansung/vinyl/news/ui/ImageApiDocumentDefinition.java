package com.hansung.vinyl.news.ui;

import com.hansung.vinyl.common.ApiDocumentDefinition;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class ImageApiDocumentDefinition extends ApiDocumentDefinition {
    public ImageApiDocumentDefinition(RestDocumentationResultHandler docResultHandler) {
        super(docResultHandler);
    }

    public RestDocumentationResultHandler 이미지_조회_api_문서() {
        return docResultHandler.document(
                pathParameters(
                        parameterWithName("imageName").description("이미지명"))
        );
    }
}

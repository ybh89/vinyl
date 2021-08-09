package com.hansung.vinyl.news.ui;

import com.hansung.vinyl.common.ApiDocumentDefinition;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class SubscribeApiDocumentDefinition extends ApiDocumentDefinition {
    public SubscribeApiDocumentDefinition(RestDocumentationResultHandler docResultHandler) {
        super(docResultHandler);
    }

    public RestDocumentationResultHandler 소식_구독_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName(AUTHORIZATION).description("jwt access token")),
                pathParameters(
                        parameterWithName("newsId").description("소식 유니크 아이디"))
        );
    }

    public RestDocumentationResultHandler 소식_구독취소_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName(AUTHORIZATION).description("jwt access token")),
                pathParameters(
                        parameterWithName("newsId").description("소식 유니크 아이디"))
        );
    }
}

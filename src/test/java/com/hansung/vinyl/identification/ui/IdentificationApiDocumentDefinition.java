package com.hansung.vinyl.identification.ui;

import com.hansung.vinyl.common.ApiDocumentDefinition;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class IdentificationApiDocumentDefinition extends ApiDocumentDefinition {
    public IdentificationApiDocumentDefinition(RestDocumentationResultHandler docResultHandler) {
        super(docResultHandler);
    }

    public RestDocumentationResultHandler 본인_인증_요청_api_문서() {
        return docResultHandler.document(
                requestFields(
                        fieldWithPath("email").description("이메일")),
                responseFields(
                        fieldWithPath("expirationData").description("만료일시"),
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("approved").description("본인 인증 완료 여부"),
                        fieldWithPath("message").description("결과 메시지"))
        );
    }

    public RestDocumentationResultHandler 본인_인증_결과_조회_api_문서() {
        return docResultHandler.document(
                requestParameters(
                        parameterWithName("email").description("이메일")),
                responseFields(
                        fieldWithPath("expirationData").description("만료일시"),
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("approved").description("본인 인증 완료 여부"),
                        fieldWithPath("message").description("결과 메시지"))
        );
    }
}

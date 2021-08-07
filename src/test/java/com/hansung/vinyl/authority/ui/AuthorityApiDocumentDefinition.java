package com.hansung.vinyl.authority.ui;

import com.hansung.vinyl.common.ApiDocumentDefinition;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;

public final class AuthorityApiDocumentDefinition extends ApiDocumentDefinition {
    public AuthorityApiDocumentDefinition(RestDocumentationResultHandler docResultHandler) {
        super(docResultHandler);
    }

    public RestDocumentationResultHandler 권한_생성_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName("Authorization").description("jwt access token")),
                requestFields(
                        fieldWithPath("name").description("권한 이름")
                                .attributes(key("constraint").value("ROLE_ 로 시작해야한다.")),
                        fieldWithPath("remark").description("권한 설명").optional(),
                        fieldWithPath("resources.[].path").description("권한에 매핑된 자원의 경로").optional()
                                .attributes(key("constraint").value("pathPattern 형식")),
                        fieldWithPath("resources.[].httpMethod").description("권한에 매핑될 자원의 http method").optional()
                                .attributes(key("constraint").value("GET, POST, PUT, PATCH, DELETE 중 택1"))),
                responseFields(
                        fieldWithPath("id").description("권한 유니크 아이디"),
                        fieldWithPath("name").description("권한 이름"),
                        fieldWithPath("remark").description("권한 설명"),
                        fieldWithPath("resources.[].path").description("권한에 매핑된 자원의 경로"),
                        fieldWithPath("resources.[].httpMethod").description("권한에 매핑될 자원의 http method"))
        );
    }

    public RestDocumentationResultHandler 권한_변경_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName("Authorization").description("jwt access token")),
                pathParameters(
                        parameterWithName("authorityId").description("권한 유니크 아이디")),
                requestFields(
                        fieldWithPath("name").description("권한 이름")
                                .attributes(key("constraint").value("ROLE_ 로 시작해야한다.")),
                        fieldWithPath("remark").description("변경할 권한 설명").optional(),
                        fieldWithPath("resources.[].path").description("권한에 매핑된 자원의 경로").optional()
                                .attributes(key("constraint").value("pathPattern 형식")),
                        fieldWithPath("resources.[].httpMethod").description("권한에 매핑될 자원의 http method").optional()
                                .attributes(key("constraint").value("GET, POST, PUT, PATCH, DELETE 중 택1"))),
                responseFields(
                        fieldWithPath("id").description("변경된 권한 유니크 아이디"),
                        fieldWithPath("name").description("변경된 권한 이름"),
                        fieldWithPath("remark").description("변경된 권한 설명"),
                        fieldWithPath("resources.[].path").description("권한에 매핑된 자원의 경로"),
                        fieldWithPath("resources.[].httpMethod").description("권한에 매핑될 자원의 http method"))
        );
    }

    public RestDocumentationResultHandler 권한_목록_조회_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName("Authorization").description("jwt access token")),
                responseFields(
                        fieldWithPath("[].id").description("권한 유니크 아이디"),
                        fieldWithPath("[].name").description("권한 이름"),
                        fieldWithPath("[].remark").description("권한 설명"),
                        fieldWithPath("[].resources.[].path").description("권한에 매핑된 자원의 경로, pathPattern"),
                        fieldWithPath("[].resources.[].httpMethod").description("권한에 매핑된 자원의 http method"))
        );
    }

    public RestDocumentationResultHandler 권한_삭제_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName("Authorization").description("jwt access token")),
                pathParameters(
                        parameterWithName("authorityId").description("삭제할 권한 유니크 아이디"))
        );
    }
}

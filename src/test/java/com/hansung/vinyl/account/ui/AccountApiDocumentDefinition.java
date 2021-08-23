package com.hansung.vinyl.account.ui;

import com.hansung.vinyl.common.ApiDocumentDefinition;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

public final class AccountApiDocumentDefinition extends ApiDocumentDefinition {
    public AccountApiDocumentDefinition(RestDocumentationResultHandler docResultHandler) {
        super(docResultHandler);
    }

    public RestDocumentationResultHandler 회원_가입_api_문서() {
        return docResultHandler.document(
                requestFields(
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("password").description("패스워드")
                                .attributes(key("constraint").value("영문과 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자")),
                        fieldWithPath("authorityIds").description("계정에 매핑될 권한").optional(),
                        fieldWithPath("name").description("사용자 이름"),
                        fieldWithPath("phone").description("폰 번호").optional(),
                        fieldWithPath("gender").description("성별").optional()
                                .attributes(key("constraint").value("MALE, FEMALE 택1")),
                        fieldWithPath("fcmToken").description("fcm token")),
                responseFields(
                        fieldWithPath("id").description("계정 유니크 아이디"),
                        fieldWithPath("email").description("이메일"))
        );
    }

    public RestDocumentationResultHandler 회원_탈퇴_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName("Authorization").description("jwt access token"))
        );
    }

    public RestDocumentationResultHandler 계정_권한_변경_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName("Authorization").description("jwt access token")),
                pathParameters(
                        parameterWithName("accountId").description("계정 유니크 아이디")),
                requestFields(
                        fieldWithPath("authorityIds").description("변경할 권한 아이디")
                )
        );
    }

    public RestDocumentationResultHandler 이메일_중복_체크_api_문서() {
        return docResultHandler.document(
                requestParameters(
                        parameterWithName("email").description("중복 검사할 이메일")),
                responseFields(
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("duplicated").description("이메일 중복 여부"),
                        fieldWithPath("message").description("결과 메시지"))
        );
    }
}

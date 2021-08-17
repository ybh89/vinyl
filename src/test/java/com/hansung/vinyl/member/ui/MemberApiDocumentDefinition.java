package com.hansung.vinyl.member.ui;

import com.hansung.vinyl.common.ApiDocumentDefinition;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

public class MemberApiDocumentDefinition extends ApiDocumentDefinition {
    public MemberApiDocumentDefinition(RestDocumentationResultHandler docResultHandler) {
        super(docResultHandler);
    }

    public RestDocumentationResultHandler 프로필_조회_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("jwt access token")),
                responseFields(
                        fieldWithPath("id").description("계정 유니크 아이디"),
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("name").description("이름"),
                        fieldWithPath("phone").description("폰번호"),
                        fieldWithPath("gender").description("성별"),
                        fieldWithPath("createdAt").description("생성일시"),
                        fieldWithPath("updatedAt").description("수정일시"))
        );
    }

    public RestDocumentationResultHandler 구독_목록_조회_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("jwt access token")),
                responseFields(
                        fieldWithPath("[].id").description("소식 유니크 아이디"),
                        fieldWithPath("[].title").description("제목"),
                        fieldWithPath("[].releaseDate").description("발매일"),
                        fieldWithPath("[].price").description("가격"),
                        fieldWithPath("[].priceType").description("화폐단위"),
                        fieldWithPath("[].topic").description("토픽"),
                        fieldWithPath("[].mainImage").description("메인 이미지"))
        );
    }

    public RestDocumentationResultHandler 회원_조회_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("jwt access token")),
                pathParameters(
                        parameterWithName("memberId").description("회원 유니크 아이디")),
                responseFields(
                        fieldWithPath("id").description("회원 유니크 아이디"),
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("name").description("이름"),
                        fieldWithPath("phone").description("폰번호"),
                        fieldWithPath("gender").description("성별"),
                        fieldWithPath("createdAt").description("생성일시"),
                        fieldWithPath("updatedAt").description("수정일시"))
        );
    }

    public RestDocumentationResultHandler 회원_목록_조회_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("jwt access token")),
                requestParameters(
                        parameterWithName("page").description("현재 페이지").optional()
                                .attributes(key(CONSTRAINT_KEY).value("0부터 시작한다.")),
                        parameterWithName("size").description("한 페이지에 노출할 요소의 수").optional(),
                        parameterWithName("sort").description("정렬 조건").optional()
                                .attributes(key(CONSTRAINT_KEY).value("id, email, name, gender, phone 중 택1(asc 생략 가능)"))),
                responseFields(
                        fieldWithPath("content.[].id").description("계정 유니크 아이디"),
                        fieldWithPath("content.[].email").description("이메일"),
                        fieldWithPath("content.[].name").description("이름"),
                        fieldWithPath("content.[].phone").description("폰번호"),
                        fieldWithPath("content.[].gender").description("성별"),
                        fieldWithPath("content.[].createdAt").description("생성일시"),
                        fieldWithPath("content.[].updatedAt").description("수정일시"),
                        fieldWithPath("pageable").description(""),
                        fieldWithPath("totalPages").description("총 페이지 수"),
                        fieldWithPath("totalElements").description("총 페이지의 모든 요소의 수"),
                        fieldWithPath("last").description("현 페이지에 대한 마지막 페이지 여부"),
                        fieldWithPath("numberOfElements").description("현 페이지의 요소의 수"),
                        fieldWithPath("first").description("현 페이지에 대한 첫 페이지 여부"),
                        fieldWithPath("number").description(""),
                        fieldWithPath("sort.sorted").description("정렬 여부"),
                        fieldWithPath("sort.unsorted").description(""),
                        fieldWithPath("sort.empty").description(""),
                        fieldWithPath("size").description(""),
                        fieldWithPath("empty").description("비어있는지 여부"))
        );
    }
}

package com.hansung.vinyl.news.ui;

import com.hansung.vinyl.common.ApiDocumentDefinition;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

public class NewsApiDocumentDefinition extends ApiDocumentDefinition {
    public NewsApiDocumentDefinition(RestDocumentationResultHandler docResultHandler) {
        super(docResultHandler);
    }

    public RestDocumentationResultHandler 소식_생성_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName(AUTHORIZATION).description("jwt access token")),
                requestParts(
                        partWithName("images").description("소식 관련 이미지 목록")
                ),
                requestParameters(
                        parameterWithName("title").description("제목"),
                        parameterWithName("content").description("내용"),
                        parameterWithName("brand").description("소식 관련 제품 회사").optional(),
                        parameterWithName("price").description("가격").optional()
                                .attributes(key(CONSTRAINT_KEY).value("가격은 정확한 가격이나 범위를 자유롭게 입력 가능. 예) 140~200")),
                        parameterWithName("priceType").description("화폐 단위").optional()
                                .attributes(key(CONSTRAINT_KEY).value("USD, WON, EURO, GBP 등.. 택1, 더 추가될 예정")),
                        parameterWithName("sourceUrl").description("소식 관련 URL").optional(),
                        parameterWithName("releaseDate").description("발매일").optional()
                                .attributes(key(CONSTRAINT_KEY).value("yyyy-MM-dd'T'HH:mm:ss")),
                        parameterWithName("topic").description("토픽 - 해당 값으로 토픽 전송된다.")
                ),
                responseFields(
                        fieldWithPath("id").description("소식 유니크 아이디"),
                        fieldWithPath("title").description("제목"),
                        fieldWithPath("content").description("내용"),
                        fieldWithPath("brand").description("소식 관련 제품 회사"),
                        fieldWithPath("sourceUrl").description("소식 관련 URL"),
                        fieldWithPath("releaseDate").description("발매일"),
                        fieldWithPath("price").description("가격"),
                        fieldWithPath("priceType").description("화폐 단위"),
                        fieldWithPath("createdAt").description("생성일시"),
                        fieldWithPath("updatedAt").description("수정일시"),
                        fieldWithPath("createdBy").description("생성자"),
                        fieldWithPath("updatedBy").description("수정자"),
                        fieldWithPath("topic").description("토픽"),
                        fieldWithPath("images.[].storeOriginalImageUrl").description("저장된 원본 이미지 Url"),
                        fieldWithPath("images.[].storeThumbnailImageUrl").description("저장된 섬네일 이미지 Url"),
                        fieldWithPath("images.[].uploadName").description("업로드한 이미지명"))
        );
    }

    public RestDocumentationResultHandler 소식_수정_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName(AUTHORIZATION).description("jwt access token")),
                requestParts(
                        partWithName("images").description("소식 관련 이미지 목록")
                ),
                pathParameters(
                        parameterWithName("newsId").description("소식 유니크 아이디")),
                requestParameters(
                        parameterWithName("title").description("제목"),
                        parameterWithName("content").description("내용"),
                        parameterWithName("brand").description("소식 관련 제품 회사").optional(),
                        parameterWithName("price").description("가격").optional()
                                .attributes(key(CONSTRAINT_KEY).value("가격은 정확한 가격이나 범위를 자유롭게 입력 가능. 예) 140~200")),
                        parameterWithName("priceType").description("화폐 단위").optional()
                                .attributes(key(CONSTRAINT_KEY).value("USD, WON, EURO, GBP 등.. 택1, 더 추가될 예정")),
                        parameterWithName("sourceUrl").description("소식 관련 URL").optional(),
                        parameterWithName("releaseDate").description("발매일").optional()
                                .attributes(key(CONSTRAINT_KEY).value("yyyy-MM-dd'T'HH:mm:ss")),
                        parameterWithName("topic").description("토픽 - 해당 값으로 토픽 전송된다.")
                ),
                responseFields(
                        fieldWithPath("id").description("소식 유니크 아이디"),
                        fieldWithPath("title").description("제목"),
                        fieldWithPath("content").description("내용"),
                        fieldWithPath("brand").description("소식 관련 제품 회사"),
                        fieldWithPath("sourceUrl").description("소식 관련 URL"),
                        fieldWithPath("releaseDate").description("발매일"),
                        fieldWithPath("price").description("가격"),
                        fieldWithPath("priceType").description("화폐 단위"),
                        fieldWithPath("createdAt").description("생성일시"),
                        fieldWithPath("updatedAt").description("수정일시"),
                        fieldWithPath("createdBy").description("생성자"),
                        fieldWithPath("updatedBy").description("수정자"),
                        fieldWithPath("topic").description("토픽"),
                        fieldWithPath("images.[].storeOriginalImageUrl").description("저장된 원본 이미지 Url"),
                        fieldWithPath("images.[].storeThumbnailImageUrl").description("저장된 섬네일 이미지 Url"),
                        fieldWithPath("images.[].uploadName").description("업로드한 이미지명"))
        );
    }

    public RestDocumentationResultHandler 소식_목록_조회_api_문서() {
        return docResultHandler.document(
                requestParameters(
                        parameterWithName("page").description("현재 페이지").optional()
                                .attributes(key(CONSTRAINT_KEY).value("0부터 시작한다.")),
                        parameterWithName("size").description("한 페이지에 노출할 요소의 수").optional(),
                        parameterWithName("sort").description("정렬 조건").optional()
                                .attributes(key(CONSTRAINT_KEY).value("id, email, name, gender, phone 중 택1(asc 생략 가능)"))),
                responseFields(
                        fieldWithPath("content.[].id").description("소식 유니크 아이디"),
                        fieldWithPath("content.[].title").description("제목"),
                        fieldWithPath("content.[].releaseDate").description("발매일"),
                        fieldWithPath("content.[].price").description("가격"),
                        fieldWithPath("content.[].priceType").description("화폐 단위"),
                        fieldWithPath("content.[].topic").description("토픽"),
                        fieldWithPath("content.[].mainImage").description("메인 이미지"),
                        fieldWithPath("pageable").description(""),
                        fieldWithPath("numberOfElements").description("현 페이지의 요소의 수"),
                        fieldWithPath("number").description(""),
                        fieldWithPath("first").description("현 페이지에 대한 첫 페이지 여부"),
                        fieldWithPath("last").description("현 페이지에 대한 마지막 페이지 여부"),
                        fieldWithPath("sort.unsorted").description(""),
                        fieldWithPath("sort.sorted").description("정렬 여부"),
                        fieldWithPath("sort.empty").description(""),
                        fieldWithPath("size").description(""),
                        fieldWithPath("empty").description("비어있는지 여부"))
        );
    }

    public RestDocumentationResultHandler 소식_상세_조회_api_문서() {
        return docResultHandler.document(
                pathParameters(
                        parameterWithName("newsId").description("소식 유니크 아이디")),
                responseFields(
                        fieldWithPath("id").description("소식 유니크 아이디"),
                        fieldWithPath("title").description("제목"),
                        fieldWithPath("content").description("내용"),
                        fieldWithPath("brand").description("소식 관련 제품 회사"),
                        fieldWithPath("sourceUrl").description("소식 관련 URL"),
                        fieldWithPath("releaseDate").description("발매일"),
                        fieldWithPath("price").description("가격"),
                        fieldWithPath("priceType").description("화폐 단위"),
                        fieldWithPath("createdAt").description("생성일시"),
                        fieldWithPath("updatedAt").description("수정일시"),
                        fieldWithPath("createdBy").description("생성자"),
                        fieldWithPath("updatedBy").description("수정자"),
                        fieldWithPath("topic").description("토픽"),
                        fieldWithPath("images.[].storeOriginalImageUrl").description("저장된 원본 이미지 Url"),
                        fieldWithPath("images.[].storeThumbnailImageUrl").description("저장된 섬네일 이미지 Url"),
                        fieldWithPath("images.[].uploadName").description("업로드한 이미지명"))
        );
    }

    public RestDocumentationResultHandler 소식_삭제_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName(AUTHORIZATION).description("jwt access token")),
                pathParameters(
                        parameterWithName("newsId").description("소식 유니크 아이디"))
        );
    }
}

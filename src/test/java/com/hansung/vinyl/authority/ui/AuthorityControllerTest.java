package com.hansung.vinyl.authority.ui;

import com.hansung.vinyl.authority.application.AuthorityService;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.authority.dto.ResourceRequest;
import com.hansung.vinyl.authority.dto.ResourceResponse;
import com.hansung.vinyl.common.ControllerTest;
import com.hansung.vinyl.common.UnsecuredWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.hansung.vinyl.authority.domain.HttpMethod.GET;
import static com.hansung.vinyl.authority.domain.HttpMethod.POST;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UnsecuredWebMvcTest(AuthorityController.class)
public class AuthorityControllerTest extends ControllerTest {
    @MockBean
    private AuthorityService authorityService;

    @DisplayName("권한 생성")
    @Test
    public void authority_create() throws Exception {
        // given
        AuthorityResponse authorityResponse = buildAuthorityResponse(1L,"ROLE_TEST", "테스트용 권한입니다.",
                asList(new ResourceResponse("/**", GET)));
        AuthorityRequest authorityRequest = buildAuthorityRequest("ROLE_TEST", "테스트용 권한입니다.",
                asList(new ResourceRequest("/**", GET)));
        when(authorityService.create(any())).thenReturn(authorityResponse);

        // when
        ResultActions resultActions = 권한_생성_요청("/authorities", authorityRequest);

        // then
        권한_생성됨(resultActions);

        // api documentation
        문서화(resultActions, 권한_생성_api_문서());
    }

    @DisplayName("권한 목록 조회")
    @Test
    public void authority_list() throws Exception {
        // given
        AuthorityResponse authorityResponse1 = buildAuthorityResponse(1L,"ROLE_TEST1", "테스트용 권한입니다.1",
                asList(new ResourceResponse("/**", GET)));
        AuthorityResponse authorityResponse2 = buildAuthorityResponse(2L,"ROLE_TEST2", "테스트용 권한입니다.2",
                asList(new ResourceResponse("/**", POST)));
        when(authorityService.list()).thenReturn(asList(authorityResponse1, authorityResponse2));

        // when
        ResultActions resultActions = 권한_목록_조회_요청("/authorities", true);

        // then
        권한_목록_조회됨(resultActions, 2);

        // api documentation
        문서화(resultActions, 권한_목록_조회_api_문서());
    }

    @DisplayName("권한 삭제")
    @Test
    public void authority_delete() throws Exception {
        // when
        ResultActions resultActions = 권한_삭제_요청("/authorities/{authorityId}", 1);

        // then
        권한_삭제됨(resultActions);

        // api documentation
        문서화(resultActions, 권한_삭제_api_문서());
    }

    @DisplayName("권한 변경")
    @Test
    public void authority_update() throws Exception {
        // given
        AuthorityRequest authorityRequest = buildAuthorityRequest("ROLE_UPDATE", "변경할 테스트 권한입니다.",
                asList(new ResourceRequest("/accounts/*", GET)));
        AuthorityResponse authorityResponse = buildAuthorityResponse(1L,"ROLE_UPDATE", "변경할 테스트 권한입니다.",
                asList(new ResourceResponse("/accounts/*", GET)));
        when(authorityService.update(anyLong(), any())).thenReturn(authorityResponse);

        // when
        ResultActions resultActions = 권한_변경_요청("/authorities/{authorityId}", 1, authorityRequest);

        // then
        권한_변경됨(resultActions);

        // api documentation
        문서화(resultActions, 권한_변경_api_문서());
    }

    private void 문서화(ResultActions resultActions, RestDocumentationResultHandler document) throws Exception {
        resultActions.andDo(document);
    }

    private RestDocumentationResultHandler 권한_변경_api_문서() {
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

    private void 권한_변경됨(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(notNullValue())));
    }

    private ResultActions 권한_변경_요청(String url, Object pathVariable, Object requestDto) throws Exception {
        return update(url, pathVariable, requestDto);
    }

    private RestDocumentationResultHandler 권한_생성_api_문서() {
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

    private ResultActions 권한_생성_요청(String url, Object requestDto) throws Exception {
        return post(url, requestDto);
    }

    private void 권한_생성됨(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(notNullValue())));
    }

    private RestDocumentationResultHandler 권한_목록_조회_api_문서() {
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

    private void 권한_목록_조회됨(ResultActions resultActions, int listSize) throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(listSize)));
    }

    private ResultActions 권한_목록_조회_요청(String url, boolean isAuthorizationRequired) throws Exception {
        return get(url, isAuthorizationRequired);
    }

    private RestDocumentationResultHandler 권한_삭제_api_문서() {
        return docResultHandler.document(
                requestHeaders(headerWithName("Authorization").description("jwt access token")),
                pathParameters(
                        parameterWithName("authorityId").description("삭제할 권한 유니크 아이디"))
        );
    }

    private void 권한_삭제됨(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isNoContent());
    }

    private ResultActions 권한_삭제_요청(String url, Object pathVariable) throws Exception {
        return delete(url, pathVariable);
    }

    private AuthorityRequest buildAuthorityRequest(String name, String remark, List<ResourceRequest> resourceRequests) {
        return AuthorityRequest.builder()
                .name(name)
                .remark(remark)
                .resources(resourceRequests)
                .build();
    }

    private AuthorityResponse buildAuthorityResponse(Long id, String name, String remark, List<ResourceResponse> resourceResponses) {
        return AuthorityResponse.builder()
                .id(id)
                .name(name)
                .remark(remark)
                .resources(resourceResponses)
                .build();
    }
}

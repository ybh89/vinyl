package com.hansung.vinyl.authority.ui;

import com.hansung.vinyl.authority.application.AuthorityService;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.authority.dto.ResourceRequest;
import com.hansung.vinyl.authority.dto.ResourceResponse;
import com.hansung.vinyl.common.ControllerTest;
import com.hansung.vinyl.common.UnsecuredWebMvcTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UnsecuredWebMvcTest(AuthorityController.class)
public class AuthorityControllerTest extends ControllerTest {
    @MockBean
    private AuthorityService authorityService;
    private AuthorityApiDocumentDefinition authorityApiDocumentDefinition;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);
        authorityApiDocumentDefinition = new AuthorityApiDocumentDefinition(docResultHandler);
    }

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
        ResultActions resultActions = post("/authorities", authorityRequest, true);

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(notNullValue())));

        // api documentation
        documentApi(resultActions, authorityApiDocumentDefinition.권한_생성_api_문서());
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
        ResultActions resultActions = get("/authorities", true);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        // api documentation
        documentApi(resultActions, authorityApiDocumentDefinition.권한_목록_조회_api_문서());
    }

    @DisplayName("권한 삭제")
    @Test
    public void authority_delete() throws Exception {
        // when
        ResultActions resultActions = delete("/authorities/{authorityId}", 1);

        // then
        resultActions.andExpect(status().isNoContent());

        // api documentation
        documentApi(resultActions, authorityApiDocumentDefinition.권한_삭제_api_문서());
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
        ResultActions resultActions = put("/authorities/{authorityId}", 1, authorityRequest);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(notNullValue())));

        // api documentation
        documentApi(resultActions, authorityApiDocumentDefinition.권한_변경_api_문서());
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

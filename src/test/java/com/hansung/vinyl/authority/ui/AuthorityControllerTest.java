package com.hansung.vinyl.authority.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansung.vinyl.authority.application.AuthorityService;
import com.hansung.vinyl.authority.domain.HttpMethod;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.authority.dto.ResourceRequest;
import com.hansung.vinyl.authority.dto.ResourceResponse;
import com.hansung.vinyl.common.ControllerTest;
import com.hansung.vinyl.common.UnsecuredWebMvcTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@UnsecuredWebMvcTest(AuthorityController.class)
public class AuthorityControllerTest extends ControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    private AuthorityService authorityService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("권한생성")
    @Test
    public void 권한생성_요청_응답확인() throws Exception {
        AuthorityResponse authorityResponse = AuthorityResponse.builder()
                .id(1L)
                .name("ROLE_TEST")
                .remark("테스트용 권한입니다.")
                .resources(Arrays.asList(new ResourceResponse("/**", HttpMethod.GET)))
                .build();
        when(authorityService.create(any())).thenReturn(authorityResponse);
        AuthorityRequest authorityRequest = AuthorityRequest.builder()
                .name("ROLE_TEST")
                .remark("테스트용 권한입니다.")
                .resources(Arrays.asList(new ResourceRequest("/**", HttpMethod.GET)))
                .build();
        mockMvc.perform(post("/authorities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(authorityRequest)))
                .andDo(document("authority",
                        requestFields(
                                fieldWithPath("name").description("권한 이름, 'ROLE_'로 시작해야한다."),
                                fieldWithPath("remark").description("권한 설명"),
                                subsectionWithPath("resources").description("권한에 매핑될 자원 목록")),
                        responseFields(
                                fieldWithPath("id").description("권한 유니크 아이디"),
                                fieldWithPath("name").description("권한 이름"),
                                fieldWithPath("remark").description("권한 설명"),
                                subsectionWithPath("resources").description("권한에 매핑된 자원 목록"))
                ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(notNullValue())))
        ;
    }

    @DisplayName("권한 목록 조회")
    @Test
    public void 권한목록조회_요청_응답확인() throws Exception {
        AuthorityResponse authorityResponse1 = AuthorityResponse.builder()
                .id(1L)
                .name("ROLE_TEST1")
                .remark("테스트 권한1")
                .resources(Arrays.asList(new ResourceResponse("/**", HttpMethod.GET)))
                .build();
        AuthorityResponse authorityResponse2 = AuthorityResponse.builder()
                .id(1L)
                .name("ROLE_TEST2")
                .remark("테스트 권한2")
                .resources(Arrays.asList(new ResourceResponse("/**", HttpMethod.POST)))
                .build();
        when(authorityService.list()).thenReturn(Arrays.asList(authorityResponse1, authorityResponse2));
        mockMvc.perform(get("/authorities")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(APPLICATION_JSON_UTF8))
                .andDo(document("authority",
                        responseFields(
                                fieldWithPath("[].id").description("권한 유니크 아이디"),
                                fieldWithPath("[].name").description("권한 이름"),
                                fieldWithPath("[].remark").description("권한 설명"),
                                fieldWithPath("[].resources.[].path").description("권한에 매핑된 자원의 경로, pathPattern"),
                                fieldWithPath("[].resources.[].httpMethod").description("권한에 매핑된 자원의 http method"))
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                ;
    }

    @DisplayName("권한 삭제")
    @Test
    public void 권한삭제_요청_응답확인() throws Exception {
        mockMvc.perform(delete("/authorities/{authorityId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(APPLICATION_JSON_UTF8))
                .andDo(document("authority",
                        pathParameters(
                                parameterWithName("authorityId").description("삭제할 권한 유니크 아이디"))
                ))
                .andExpect(status().isNoContent())
                ;
    }

    @DisplayName("권한 변경")
    @Test
    public void 권한변경_요청_응답확인() throws Exception {
        AuthorityRequest authorityRequest = AuthorityRequest.builder()
                .name("ROLE_UPDATE")
                .remark("변경할 테스트 권한입니다.")
                .resources(Arrays.asList(new ResourceRequest("/accounts/*", HttpMethod.GET)))
                .build();

        AuthorityResponse authorityResponse = AuthorityResponse.builder()
                .id(1L)
                .name("ROLE_UPDATE")
                .remark("변경할 테스트 권한입니다.")
                .resources(Arrays.asList(new ResourceResponse("/accounts/*", HttpMethod.GET)))
                .build();
        when(authorityService.update(anyLong(), any())).thenReturn(authorityResponse);

        mockMvc.perform(put("/authorities/{authorityId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(authorityRequest)))
                .andDo(document("authority",
                        pathParameters(
                                parameterWithName("authorityId").description("권한 유니크 아이디")),
                        requestFields(
                                fieldWithPath("name").description("변경할 권한 이름, 'ROLE_'로 시작해야한다."),
                                fieldWithPath("remark").description("변경할 권한 설명"),
                                subsectionWithPath("resources").description("변경할 권한에 매핑된 자원 목록")),
                        responseFields(
                                fieldWithPath("id").description("변경된 권한 유니크 아이디"),
                                fieldWithPath("name").description("변경된 권한 이름"),
                                fieldWithPath("remark").description("변경된 권한 설명"),
                                subsectionWithPath("resources").description("변경된 권한에 매핑된 자원 목록"))
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(notNullValue())))
        ;
   }
}

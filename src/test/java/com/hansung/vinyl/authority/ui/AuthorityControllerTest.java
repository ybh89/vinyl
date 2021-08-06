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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

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
        mockMvc.perform(RestDocumentationRequestBuilders.post("/authorities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(authorityRequest)))
                .andDo(document("authority",
                        /*pathParameters(
                                parameterWithName("name").description("권한 이름, 'ROLE_'로 시작해야한다. "),
                                parameterWithName("remark").description("권한 설명"),
                                parameterWithName("resources").description("권한에 매핑될 자원 목록")
                        ),*/
                        requestFields(
                                fieldWithPath("name").description("권한 이름, 'ROLE_'로 시작해야한다."),
                                fieldWithPath("remark").description("권한 설명"),
                                subsectionWithPath("resources.[]").description("권한에 매핑될 자원 목록")),
                        responseFields(
                                fieldWithPath("id").description("권한 유니크 아이디"),
                                fieldWithPath("name").description("권한 이름"),
                                fieldWithPath("remark").description("권한 설명"),
                                subsectionWithPath("resources.[]").description("권한에 매핑된 자원 목록")
                        )
                ))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(notNullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(notNullValue())))
        ;
    }
}

package com.hansung.vinyl.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "vinyl.o-r.kr")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class ControllerTest {
    public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
    private static final String TEMPORARY_JWT_TOKEN = "jwt access token";
    private static final String DOCUMENT_IDENTIFIER = "{method-name}";

    @Autowired
    private WebApplicationContext context;
    @Autowired
    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected RestDocumentationResultHandler docResultHandler;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.docResultHandler = document(DOCUMENT_IDENTIFIER,
                preprocessRequest(modifyUris().scheme("https").host("vinyl.o-r.kr").removePort(), prettyPrint()),
                preprocessResponse(prettyPrint()));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation).uris()
                        .withScheme("https")
                        .withHost("vinyl.o-r.kr")
                        .withPort(443))
                .alwaysDo(docResultHandler)
                .build();
    }

    protected ResultActions post(String url, Object requestDto, boolean isAuthorizationRequired) throws Exception {
        if (!isAuthorizationRequired) {
            return post(url, requestDto);
        }
        return mockMvc.perform(RestDocumentationRequestBuilders.post(url)
                .header(AUTHORIZATION, TEMPORARY_JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto)));

    }

    protected ResultActions post(String url, Object requestDto) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto)));
    }

    protected ResultActions get(String url, Object pathVariable, MultiValueMap params, boolean isAuthorizationRequired) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder;

        if (Objects.isNull(pathVariable)) {
            mockHttpServletRequestBuilder = RestDocumentationRequestBuilders.get(url);
        } else {
            mockHttpServletRequestBuilder = RestDocumentationRequestBuilders.get(url, pathVariable);
        }

        if (isAuthorizationRequired) {
            mockHttpServletRequestBuilder.header(AUTHORIZATION, TEMPORARY_JWT_TOKEN);
        }

        if (Objects.nonNull(params)) {
            mockHttpServletRequestBuilder.params(params);
        }

        return mockMvc.perform(mockHttpServletRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(APPLICATION_JSON_UTF8));
    }

    protected ResultActions delete(String url, Object pathVariable) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.delete(url, pathVariable)
                .header(AUTHORIZATION, TEMPORARY_JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(APPLICATION_JSON_UTF8));
    }

    protected ResultActions put(String url, Object pathVariable, Object requestDto) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.put(url, pathVariable)
                .header(AUTHORIZATION, TEMPORARY_JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto)));
    }

    protected ResultActions postWithMultipart(String url, Object pathVariable, MultiValueMap params,
                                              MockMultipartFile file) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.multipart(url, pathVariable).file(file)
                .header(AUTHORIZATION, TEMPORARY_JWT_TOKEN)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(APPLICATION_JSON_UTF8)
                .params(params));
    }

    protected ResultActions putWithMultipart(String url, Object pathVariable, MultiValueMap params,
                                             MockMultipartFile file) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = RestDocumentationRequestBuilders.fileUpload(url, pathVariable);
        builder.with(request -> {
                    request.setMethod("PUT");
                    return request;
                });
        return mockMvc.perform(builder.file(file)
                .header(AUTHORIZATION, TEMPORARY_JWT_TOKEN)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(APPLICATION_JSON_UTF8)
                .params(params));
    }

    protected void documentApi(ResultActions resultActions, RestDocumentationResultHandler document) throws Exception {
        resultActions.andDo(document)
                .andDo(print());
    }
}

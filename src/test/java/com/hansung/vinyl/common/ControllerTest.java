package com.hansung.vinyl.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

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
        this.docResultHandler = document(DOCUMENT_IDENTIFIER, preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
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

    protected ResultActions get(String url, boolean isAuthorizationRequired) throws Exception {
        if (!isAuthorizationRequired) {
            return get(url);
        }
        return mockMvc.perform(RestDocumentationRequestBuilders.get(url)
                .header(AUTHORIZATION, TEMPORARY_JWT_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(APPLICATION_JSON_UTF8));
    }

    protected ResultActions get(String url) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.get(url)
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

    protected void documentApi(ResultActions resultActions, RestDocumentationResultHandler document) throws Exception {
        resultActions.andDo(document);
    }
}

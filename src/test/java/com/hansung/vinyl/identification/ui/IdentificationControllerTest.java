package com.hansung.vinyl.identification.ui;

import com.hansung.vinyl.common.ControllerTest;
import com.hansung.vinyl.common.UnsecuredWebMvcTest;
import com.hansung.vinyl.identification.application.IdentificationService;
import com.hansung.vinyl.identification.dto.IdentificationRequest;
import com.hansung.vinyl.identification.dto.IdentificationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("본인인증 컨트롤러 테스트")
@UnsecuredWebMvcTest(IdentificationController.class)
public class IdentificationControllerTest extends ControllerTest {
    @MockBean
    private IdentificationService identificationService;
    private IdentificationApiDocumentDefinition identificationApiDocumentDefinition;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);
        identificationApiDocumentDefinition = new IdentificationApiDocumentDefinition(docResultHandler);
    }

    @DisplayName("본인 인증 요청")
    @Test
    public void identification_create() throws Exception {
        // given
        IdentificationResponse identificationResponse = new IdentificationResponse(LocalDateTime.now(),
                "identification@test.com", false,
                "본인 인증 이메일이 전송되었습니다. 이메일을 확인하여 인증을 완료해주세요.");
        when(identificationService.certify(any())).thenReturn(identificationResponse);
        IdentificationRequest identificationRequest = new IdentificationRequest("identification@test.com");

        // when
        ResultActions resultActions = post("/v1/identifications", identificationRequest, false);

        // then
        resultActions.andExpect(status().isCreated());

        // api documentation
        documentApi(resultActions, identificationApiDocumentDefinition.본인_인증_요청_api_문서());
    }

    @DisplayName("본인 인증 결과 조회")
    @Test
    public void identification_result() throws Exception {
        // given
        IdentificationResponse identificationResponse = new IdentificationResponse(LocalDateTime.now(),
                "identification@test.com", true, "정상 처리되었습니다.");
        when(identificationService.result(any())).thenReturn(identificationResponse);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("email", "identification@test.com");

        // when
        ResultActions resultActions = get("/v1/identifications", null, params, false);

        // then
        resultActions.andExpect(status().isOk());

        // api documentation
        documentApi(resultActions, identificationApiDocumentDefinition.본인_인증_결과_조회_api_문서());
    }
}

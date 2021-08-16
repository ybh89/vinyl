package com.hansung.vinyl.member.ui;

import com.hansung.vinyl.common.ControllerTest;
import com.hansung.vinyl.common.UnsecuredWebMvcTest;
import com.hansung.vinyl.member.application.MemberService;
import com.hansung.vinyl.member.domain.Gender;
import com.hansung.vinyl.member.dto.MemberResponse;
import com.hansung.vinyl.news.domain.PriceType;
import com.hansung.vinyl.news.dto.NewsListResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.hansung.vinyl.account.ui.AccountControllerTest.EMAIL;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원 컨트롤러 테스트")
@UnsecuredWebMvcTest(MemberController.class)
public class MemberControllerTest extends ControllerTest {
    @MockBean
    private MemberService memberService;
    private MemberApiDocumentDefinition memberApiDocumentDefinition;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);
        memberApiDocumentDefinition = new MemberApiDocumentDefinition(docResultHandler);
    }

    @DisplayName("프로필 조회")
    @WithMockUser
    @Test
    public void member_me() throws Exception {
        // given
        MemberResponse memberResponse = buildMemberResponse();
        when(memberService.me(any())).thenReturn(memberResponse);

        // when
        ResultActions resultActions = get("/members/me", null, null,true);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(notNullValue())))
                .andExpect(jsonPath("$.email", Matchers.is(notNullValue())));

        // api documentation
        documentApi(resultActions, memberApiDocumentDefinition.프로필_조회_api_문서());
    }

    @DisplayName("구독 목록 조회")
    @WithMockUser
    @Test
    public void member_subscribeList() throws Exception {
        //given
        NewsListResponse newsListResponse = buildNewsListResponse();
        when(memberService.subscribes(any())).thenReturn(Arrays.asList(newsListResponse));

        //when
        ResultActions resultActions = get("/members/subscribes", null,null,true);

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$..['id']").exists())
                .andExpect(jsonPath("$..['title']").exists());

        // api documentation
        documentApi(resultActions, memberApiDocumentDefinition.구독_목록_조회_api_문서());
    }
    
    @DisplayName("회원 조회")
    @Test
    public void member() throws Exception {
        //given
        MemberResponse memberResponse = buildMemberResponse();
        when(memberService.member(anyLong())).thenReturn(memberResponse);
        
        //when
        ResultActions resultActions = get("/members/{memberId}", 1L, null,true);

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(notNullValue())))
                .andExpect(jsonPath("$.email", Matchers.is(notNullValue())));

        // api documentation
        documentApi(resultActions, memberApiDocumentDefinition.회원_조회_api_문서());
    }

    @DisplayName("회원 목록 조회")
    @Test
    public void member_list() throws Exception {
        //given
        MemberResponse memberResponse = buildMemberResponse();
        when(memberService.list(any())).thenReturn(new PageImpl<>(Arrays.asList(memberResponse)));
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.set("page", "0");
        params.set("size", "3");
        params.set("sort", "id,desc");

        //when
        ResultActions resultActions = get("/members", null, params,true);

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$..['id']").exists())
                .andExpect(jsonPath("$..['email']").exists());

        // api documentation
        documentApi(resultActions, memberApiDocumentDefinition.회원_목록_조회_api_문서());
    }

    private NewsListResponse buildNewsListResponse() {
        return NewsListResponse.builder()
                .id(1L)
                .price("140")
                .priceType(PriceType.USD)
                .releaseDate(LocalDateTime.now())
                .title("testTitle")
                .build();
    }

    private MemberResponse buildMemberResponse() {
        return MemberResponse.builder()
                .id(1L)
                .email(EMAIL)
                .name("testName")
                .gender(Gender.MALE)
                .phone("01011111111")
                .build();
    }
}

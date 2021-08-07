package com.hansung.vinyl.account.ui;

import com.hansung.vinyl.account.dto.JoinRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "test-password123";

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("회원가입")
    @Test
    public void 회원가입_요청_응답확인() throws Exception {
        JoinRequest joinRequest = JoinRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .name("윤병현")
                .build();
        mockMvc.perform(post("/accounts")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}

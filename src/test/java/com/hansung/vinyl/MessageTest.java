package com.hansung.vinyl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@SpringBootTest
public class MessageTest {
    @Autowired
    MessageSource messageSource;

    @Test
    public void __() throws Exception {
        String email = messageSource.getMessage("Email", null, null);
        System.out.println(email);
    }
}

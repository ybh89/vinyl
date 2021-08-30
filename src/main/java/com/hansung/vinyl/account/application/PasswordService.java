package com.hansung.vinyl.account.application;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;

@Component
public class PasswordService {
    private static final char[] CHAR_SET = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
            'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&'};
    private static final int PASSWORD_LENGTH = 14;

    public String createRandomPassword() {
        StringBuffer sb = new StringBuffer();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(new Date().getTime());
        int idx = 0;
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            idx = secureRandom.nextInt(CHAR_SET.length);
            sb.append(CHAR_SET[idx]);
        }
        return sb.toString();
    }
}

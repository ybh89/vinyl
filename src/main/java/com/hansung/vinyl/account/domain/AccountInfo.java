package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.authority.domain.Authority;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AccountInfo {
    private String email;
    private String encryptedPassword;
    private List<Authority> authorities;
    private String provider;
    private String providerId;
}

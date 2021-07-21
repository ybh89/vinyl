package com.hansung.vinyl.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class AccountRequest {
    private String email;
    private String password;
    private List<String> roleIds;

    @Builder
    public AccountRequest(String email, String password, List<String> roleIds) {
        this.email = email;
        this.password = password;
        this.roleIds = roleIds;
    }
}

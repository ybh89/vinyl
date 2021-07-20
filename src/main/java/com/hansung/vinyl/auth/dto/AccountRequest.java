package com.hansung.vinyl.auth.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountRequest {
    private String email;
    private String password;
    private List<String> roleIds;
}

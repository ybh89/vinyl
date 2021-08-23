package com.hansung.vinyl.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerifyEmailResponse {
    private String email;
    private boolean isDuplicated;
    private String message;
}

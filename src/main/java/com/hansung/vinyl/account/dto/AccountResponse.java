package com.hansung.vinyl.account.dto;

import com.hansung.vinyl.account.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AccountResponse {
    private Long id;
    private String email;

    public AccountResponse(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static AccountResponse of(Account savedAccount) {
        return new AccountResponse(savedAccount.getId(), savedAccount.getEmail());
    }
}

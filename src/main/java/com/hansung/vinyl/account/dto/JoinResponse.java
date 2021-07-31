package com.hansung.vinyl.account.dto;

import com.hansung.vinyl.account.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class JoinResponse {
    private Long id;
    private String email;

    public JoinResponse(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static JoinResponse of(Account savedAccount) {
        return new JoinResponse(savedAccount.getId(), savedAccount.getEmail());
    }
}

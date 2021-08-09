package com.hansung.vinyl.account.dto;

import com.hansung.vinyl.account.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinResponse {
    private Long id;
    private String email;

    public static JoinResponse of(Account savedAccount) {
        return new JoinResponse(savedAccount.getId(), savedAccount.getEmail().value());
    }
}

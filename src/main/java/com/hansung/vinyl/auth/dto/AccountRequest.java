package com.hansung.vinyl.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.List;

@NoArgsConstructor
@Data
public class AccountRequest {
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문과 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;
    private List<String> roleIds;

    @Builder
    public AccountRequest(String email, String password, List<String> roleIds) {
        this.email = email;
        this.password = password;
        this.roleIds = roleIds;
    }
}

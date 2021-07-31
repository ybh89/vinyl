package com.hansung.vinyl.account.dto;

import com.hansung.vinyl.account.domain.Join;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinRequest {
    @Email
    private String email;
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}")
    private String password;
    private List<Long> authorityIds = new ArrayList<>();
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    private String gender;

    public Join toJoin() {
        return new Join(email, name, phone, gender);
    }

    public JoinRequest(String email, String password, List<Long> authorityIds) {
        this.email = email;
        this.password = password;
        this.authorityIds = authorityIds;
    }
}

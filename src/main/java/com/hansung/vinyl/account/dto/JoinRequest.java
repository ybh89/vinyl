package com.hansung.vinyl.account.dto;

import com.hansung.vinyl.member.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinRequest {
    @Email
    private String email;
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}")
    private String password;
    private List<Long> authorityIds;
    @NotBlank
    private String name;
    private String phone;
    private Gender gender;
    @NotBlank
    private String fcmToken;
}

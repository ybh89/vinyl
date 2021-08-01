package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.member.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreatedEvent {
    private Long accountId;
    private String email;
    private String name;
    private String phone;
    private Gender gender;
}

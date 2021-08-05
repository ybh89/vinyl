package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.member.domain.Gender;
import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
public class AccountCreatedEvent {
    private Long accountId;
    private String email;
    private String name;
    private String phone;
    private Gender gender;
    private String fcmToken;
}

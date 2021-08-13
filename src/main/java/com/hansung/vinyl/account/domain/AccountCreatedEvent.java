package com.hansung.vinyl.account.domain;

import lombok.*;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreatedEvent {
    private Account account;
    private MemberInfo memberInfo;
}

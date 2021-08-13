package com.hansung.vinyl.account.domain;

import com.hansung.vinyl.member.domain.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfo {
    private String name;
    private String phone;
    private Gender gender;
    private String fcmToken;
}

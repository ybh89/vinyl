package com.hansung.vinyl.account.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Join {
    private String email;
    private String name;
    private String phone;
    private String gender;

    @Builder
    public Join(String email, String name, String phone, String gender) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
    }
}

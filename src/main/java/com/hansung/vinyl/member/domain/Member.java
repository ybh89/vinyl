package com.hansung.vinyl.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {
    @Id
    private Long id;

    @Column(unique = true, nullable = false, updatable = false, length = 50)
    private String email;

    @Column(nullable = false, updatable = false, length = 50)
    private String name;
    private String phone;
    private Gender gender;
    private List<Long> favorites = new ArrayList<>();

    @Builder
    public Member(Long accountId, String email, String name, String phone, Gender gender, List<Long> favorites) {
        this.id = accountId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.favorites = favorites;
    }
}

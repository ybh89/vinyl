package com.hansung.vinyl.member.domain;

import com.hansung.vinyl.common.domain.BaseDateTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseDateTimeEntity {
    @Id
    private Long id;

    @Column(unique = true, nullable = false, updatable = false, length = 50)
    private String email;

    @Column(nullable = false, updatable = false, length = 50)
    private String name;
    private String phone;
    private Gender gender;

    @OneToMany(mappedBy = "member")
    private List<Subscribe> subscribes = new ArrayList<>();

    @Builder
    public Member(Long accountId, String email, String name, String phone, Gender gender) {
        this.id = accountId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
    }

    public void subscribe(Long newsId) {
        Subscribe subscribe = buildSubscribe(newsId);
        if (!subscribes.contains(subscribe)) {
            subscribes.add(subscribe);
        }
    }

    public void unsubscribe(Long newsId) {
        Subscribe subscribe = buildSubscribe(newsId);
        subscribes.remove(subscribe);
    }

    private Subscribe buildSubscribe(Long newsId) {
        return Subscribe.builder()
                .member(this)
                .newsId(newsId)
                .build();
    }
}

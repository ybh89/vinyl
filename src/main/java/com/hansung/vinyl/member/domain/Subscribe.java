package com.hansung.vinyl.member.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints={ @UniqueConstraint(name = "uk_subscribe", columnNames = {"member_id", "news_id"}) })
@Entity
public class Subscribe {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    @Column(name = "news_id")
    private Long newsId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscribe subscribe = (Subscribe) o;
        return Objects.equals(getMember(), subscribe.getMember()) && Objects.equals(getNewsId(), subscribe.getNewsId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMember(), getNewsId());
    }
}

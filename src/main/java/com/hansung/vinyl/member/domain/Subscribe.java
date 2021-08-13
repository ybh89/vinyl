package com.hansung.vinyl.member.domain;

import com.hansung.vinyl.common.exception.validate.FormatException;
import com.hansung.vinyl.common.exception.validate.NullException;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints={ @UniqueConstraint(name = "uk_subscribe", columnNames = {"member_id", "news_id"}) })
@Embeddable
public class Subscribe {
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_subscribe"))
    @Column(name = "news_id")
    private Long newsId;

    public Subscribe(Long newsId) {
        validate(newsId);
        this.newsId = newsId;
    }

    private void validate(Long newsId) {
        if (Objects.isNull(newsId)) {
            throw new NullException("newsId", getClass().getName());
        }

        if (newsId < 1) {
            throw new FormatException("newsId", newsId, getClass().getName());
        }
    }
}

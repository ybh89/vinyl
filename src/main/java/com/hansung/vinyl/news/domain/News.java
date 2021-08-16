package com.hansung.vinyl.news.domain;

import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor(access = PROTECTED)
@SecondaryTable(
        name = "image",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "news_id")
)
@Table(uniqueConstraints={ @UniqueConstraint(name = "uk_news_topic", columnNames = "topic") })
@Entity
public class News extends BaseEntity {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private Catalog catalog;

    @Embedded
    private Post post;

    private News(Catalog catalog, Post post) {
        this.catalog = catalog;
        this.post = post;
    }

    public static News create(Catalog catalog, Post post) {
        return new News(catalog, post);
    }

    public void update(News updateNews) {
        this.post = updateNews.post;
        this.catalog = updateNews.catalog;
    }

    public void delete() {
        post.delete();
    }

    public void plusSubscribeCount() {
        post.plusSubscribeCount();
    }

    public void minusSubscribeCount() {
        post.minusSubscribeCount();
    }

    public Images getImages() {
        return post.getImages();
    }

    public void updateImages(Images images) {
        post.updateImages(images);
    }

    public boolean isOwner(User user) {
        if (user.hasSuperRole()) {
            return true;
        }
        return getCreatedBy().equals(user.getAccountId());
    }
}

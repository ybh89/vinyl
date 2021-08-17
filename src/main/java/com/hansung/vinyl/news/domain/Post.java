package com.hansung.vinyl.news.domain;

import com.google.common.base.Strings;
import com.hansung.vinyl.common.exception.validate.BlankException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Access(FIELD)
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Post {
    private static final int MAIN_IMAGE_SEQ = 0;

    @Column(nullable = false, length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column
    private int subscribeCount;

    private boolean deleted;

    @Column(nullable = false, updatable = false)
    private String topic;

    @Embedded
    private Images images;

    @Builder
    public Post(String title, String content, String topic, Images images) {
        validate(title, content, topic);
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.images = images;
    }

    private void validate(String title, String content, String topic) {
        if (Strings.isNullOrEmpty(title) || title.isBlank()) {
            throw new BlankException("title", title, getClass().getName());
        }

        if (Strings.isNullOrEmpty(content) || content.isBlank()) {
            throw new BlankException("content", content, getClass().getName());
        }

        if (Strings.isNullOrEmpty(topic) || topic.isBlank()) {
            throw new BlankException("topic", topic, getClass().getName());
        }
    }

    public void delete() {
        deleted = true;
    }

    public void plusSubscribeCount() {
        subscribeCount++;
    }

    public void minusSubscribeCount() {
        if (subscribeCount > 0) {
            subscribeCount--;
        }
    }

    public void updateImages(Images images) {
        this.images = images;
    }

    public Image getMainImage() {
        return images.getImage(MAIN_IMAGE_SEQ);
    }
}

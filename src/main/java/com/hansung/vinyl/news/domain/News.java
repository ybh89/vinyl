package com.hansung.vinyl.news.domain;

import com.hansung.vinyl.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class News extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 50)
    private String brand;

    @Lob
    @Column(nullable = false)
    private String content;

    private String sourceUrl;

    private LocalDateTime releaseDate;

    @Embedded
    private Price price;

    private boolean deleted;

    @Column(unique = true, nullable = false, updatable = false)
    private String topic;

    @OneToMany(mappedBy = "news", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder
    public News(Long id, String title, String brand, String content, String sourceUrl, LocalDateTime releaseDate,
                Price price, String topic, List<Image> images) {
        this.id = id;
        this.title = title;
        this.brand = brand;
        this.content = content;
        this.sourceUrl = sourceUrl;
        this.releaseDate = releaseDate;
        this.price = price;
        this.topic = topic;
        setImages(images);
    }

    public void setImages(List<Image> images) {
        this.images.clear();
        if (Objects.isNull(images)) {
            return;
        }
        this.images.addAll(images);
        images.forEach(image -> image.setNews(this));
    }

    public void update(News updateNews, List<Image> images) {
        this.title = updateNews.getTitle();
        this.content = updateNews.getContent();
        this.sourceUrl = updateNews.getSourceUrl();
        this.releaseDate = updateNews.getReleaseDate();
        this.price = updateNews.getPrice();
        setImages(images);
    }

    public void delete() {
        this.deleted = true;
    }
}

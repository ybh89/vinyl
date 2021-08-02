package com.hansung.vinyl.news.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class News {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long writer;
    @Column(nullable = false, length = 50)
    private String title;
    @Lob
    @Column(nullable = false)
    private String content;
    private String sourceUrl;
    private LocalDateTime releaseDate;
    @Embedded
    private Price price;
    @OneToMany(mappedBy = "news", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder
    public News(Long id, Long writer, String title, String content, String sourceUrl, LocalDateTime releaseDate, Price price, List<Image> images) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.sourceUrl = sourceUrl;
        this.releaseDate = releaseDate;
        this.price = price;
        setImages(images);
    }

    public void setImages(List<Image> images) {
        this.images.clear();
        this.images.addAll(images);
        images.forEach(image -> image.setNews(this));
    }
}

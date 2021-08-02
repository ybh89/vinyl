package com.hansung.vinyl.news.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Image {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false, length = 50)
    private String storeName;
    @Column(nullable = false, length = 30)
    private String uploadName;
    @Column(nullable = false)
    private int seq;
    @JoinColumn(name = "news_id")
    @ManyToOne
    private News news;

    @Builder
    public Image(Long id, String storeName, String uploadName, int seq, News news) {
        this.id = id;
        this.storeName = storeName;
        this.uploadName = uploadName;
        this.seq = seq;
        this.news = news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}

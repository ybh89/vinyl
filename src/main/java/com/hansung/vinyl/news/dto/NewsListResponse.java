package com.hansung.vinyl.news.dto;

import com.hansung.vinyl.news.domain.Catalog;
import com.hansung.vinyl.news.domain.News;
import com.hansung.vinyl.news.domain.Post;
import com.hansung.vinyl.news.domain.PriceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsListResponse {
    private Long id;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime releaseDate;
    private String price;
    private PriceType priceType;
    private String topic;
    private ImageResponse mainImage;

    public static NewsListResponse of(News news) {
        Post post = news.getPost();
        Catalog catalog = news.getCatalog();
        return new NewsListResponse(news.getId(), post.getTitle(), catalog.getReleaseDate(), catalog.getPrice().value(),
                catalog.getPrice().getPriceType(), post.getTopic(), ImageResponse.of(post.getMainImage()));
    }
}

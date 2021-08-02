package com.hansung.vinyl.news.dto;

import com.hansung.vinyl.news.domain.News;
import com.hansung.vinyl.news.domain.PriceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsListResponse {
    private Long id;
    private String title;
    private LocalDateTime releaseDate;
    private String price;
    private PriceType priceType;
    private byte[] mainThumbnailImage;

    public static NewsListResponse of(News news, byte[] mainThumbnailImage) {
        return new NewsListResponse(news.getId(), news.getTitle(), news.getReleaseDate(), news.getPrice().getPrice(),
                news.getPrice().getPriceType(), mainThumbnailImage);
    }
}

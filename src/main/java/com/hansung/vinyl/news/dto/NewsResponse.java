package com.hansung.vinyl.news.dto;

import com.hansung.vinyl.news.domain.News;
import com.hansung.vinyl.news.domain.PriceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsResponse {
    private Long id;
    private String title;
    private String content;
    private String sourceUrl;
    private LocalDateTime releaseDate;
    private String price;
    private PriceType priceType;
    private byte[] mainThumbnailImage;
    private List<ImageResponse> images = new ArrayList<>();

    public static NewsResponse of(News news, byte[] mainThumbnailImage) {
        List<ImageResponse> images = news.getImages().stream()
                .map(ImageResponse::of)
                .collect(Collectors.toList());

        return new NewsResponse(news.getId(), news.getTitle(), news.getContent(),
                news.getSourceUrl(), news.getReleaseDate(), news.getPrice().getPrice(), news.getPrice().getPriceType(),
                mainThumbnailImage, images);
    }
}

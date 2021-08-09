package com.hansung.vinyl.news.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hansung.vinyl.news.domain.News;
import com.hansung.vinyl.news.domain.PriceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsResponse {
    private Long id;
    private String title;
    private String content;
    private String brand;
    private String sourceUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime releaseDate;
    private String price;
    private PriceType priceType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private String topic;
    private byte[] mainThumbnailImage;
    private List<ImageResponse> images = new ArrayList<>();

    public static NewsResponse of(News news, byte[] mainThumbnailImage) {
        List<ImageResponse> images = news.getImages().stream()
                .map(ImageResponse::of)
                .collect(Collectors.toList());

        return new NewsResponse(news.getId(), news.getTitle(), news.getContent(), news.getBrand(),
                news.getSourceUrl(), news.getReleaseDate(), news.getPrice().getPrice(), news.getPrice().getPriceType(),
                news.getCreatedAt(), news.getUpdatedAt(), news.getCreatedBy(), news.getUpdatedBy(), news.getTopic(),
                mainThumbnailImage, images);
    }
}

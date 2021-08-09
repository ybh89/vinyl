package com.hansung.vinyl.news.dto;

import com.hansung.vinyl.news.domain.News;
import com.hansung.vinyl.news.domain.Price;
import com.hansung.vinyl.news.domain.PriceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsRequest {
    @NotBlank
    private String title;
    private String brand;
    @NotBlank
    private String content;
    @URL
    private String sourceUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime releaseDate;
    private String price;
    private PriceType priceType;
    @NotBlank
    private String topic;
    private List<MultipartFile> images;

    public News toNews() {
        return News.builder()
                .title(title)
                .content(content)
                .sourceUrl(sourceUrl)
                .releaseDate(releaseDate)
                .price(new Price(price, priceType))
                .build();
    }
}

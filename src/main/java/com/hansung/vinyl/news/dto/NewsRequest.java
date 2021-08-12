package com.hansung.vinyl.news.dto;

import com.hansung.vinyl.news.domain.*;
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
    private String catalogName;
    private String brand;
    @URL
    private String sourceUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime releaseDate;
    private String price;
    private PriceType priceType;

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String topic;
    private List<MultipartFile> images;

    public News toNews() {
        return new News(toCatalog(), toPost());
    }

    public Post toPost() {
        return Post.builder()
                .title(title)
                .content(content)
                .topic(topic)
                .build();
    }

    public Catalog toCatalog() {
        return Catalog.builder()
                .name(catalogName)
                .brand(brand)
                .price(new Price(price, priceType))
                .releaseDate(releaseDate)
                .sourceUrl(new Url(sourceUrl))
                .build();
    }
}

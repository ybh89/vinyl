package com.hansung.vinyl.news.dto;

import com.hansung.vinyl.news.domain.Image;
import com.hansung.vinyl.news.domain.News;
import com.hansung.vinyl.news.domain.Price;
import com.hansung.vinyl.news.domain.PriceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @URL
    private String sourceUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime releaseDate;
    private String price;
    private PriceType priceType;
    private List<MultipartFile> images = new ArrayList<>();

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

package com.hansung.vinyl.news.dto;

import com.hansung.vinyl.news.domain.PriceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
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
    private LocalDateTime releaseDate;
    private String price;
    private PriceType priceType;
    private List<MultipartFile> images = new ArrayList<>();
}

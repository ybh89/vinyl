package com.hansung.vinyl.news.dto;

import com.hansung.vinyl.news.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageResponse {
    private Long id;
    private String storeName;
    private String uploadName;
    private int seq;
    private Long newsId;

    public static ImageResponse of(Image image) {
        return new ImageResponse(image.getId(), image.getStoreName(), image.getUploadName(), image.getSeq(),
                image.getNews().getId());
    }
}

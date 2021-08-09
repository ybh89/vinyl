package com.hansung.vinyl.news.dto;

import com.hansung.vinyl.news.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.hansung.vinyl.news.domain.service.ImageStore.ORIGINAL_IMAGE_PREFIX;
import static com.hansung.vinyl.news.domain.service.ImageStore.THUMBNAIL_IMAGE_PREFIX;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageResponse {
    private Long id;
    private String storeOriginalImageName;
    private String storeThumbnailImageName;
    private String uploadName;
    private int seq;
    private Long newsId;

    public static ImageResponse of(Image image) {
        return new ImageResponse(image.getId(), ORIGINAL_IMAGE_PREFIX + image.getStoreName(),
                THUMBNAIL_IMAGE_PREFIX + image.getStoreName(), image.getUploadName(),
                image.getSeq(), image.getNews().getId());
    }
}

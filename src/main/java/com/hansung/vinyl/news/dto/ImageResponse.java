package com.hansung.vinyl.news.dto;

import com.hansung.vinyl.news.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.hansung.vinyl.news.domain.service.LocalImageStore.ORIGINAL_IMAGE_PREFIX;
import static com.hansung.vinyl.news.domain.service.LocalImageStore.THUMBNAIL_IMAGE_PREFIX;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageResponse {
    private String storeOriginalImageName;
    private String storeThumbnailImageName;
    private String uploadName;

    public static ImageResponse of(Image image) {
        return new ImageResponse(ORIGINAL_IMAGE_PREFIX + image.getStoreName(),
                THUMBNAIL_IMAGE_PREFIX + image.getStoreName(), image.getUploadName());
    }
}

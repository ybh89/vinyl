package com.hansung.vinyl.news.dto;

import com.hansung.vinyl.news.domain.Image;
import com.hansung.vinyl.news.domain.service.ImageStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.hansung.vinyl.news.domain.service.LocalImageStore.ORIGINAL_IMAGE_PREFIX;
import static com.hansung.vinyl.news.domain.service.LocalImageStore.THUMBNAIL_IMAGE_PREFIX;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageResponse {
    private String storeOriginalImageUrl;
    private String storeThumbnailImageUrl;
    private String uploadName;

    public static ImageResponse of(Image image, ImageStore imageStore) {
        if (Objects.isNull(image)) {
            return null;
        }
        return new ImageResponse(imageStore.getImageUrl(ORIGINAL_IMAGE_PREFIX + image.getStoreName()),
                imageStore.getImageUrl(THUMBNAIL_IMAGE_PREFIX + image.getStoreName()), image.getUploadName());
    }
}

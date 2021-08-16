package com.hansung.vinyl.news.domain.service;

import com.hansung.vinyl.news.domain.Images;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageStore {
    String ORIGINAL_IMAGE_PREFIX = "original-";
    String THUMBNAIL_IMAGE_PREFIX = "thumbnail-";
    double THUMBNAIL_RATIO = 3;

    Images uploadImages(List<MultipartFile> multipartFiles);
    String getImageUrl(String imageName);
    void deleteImages(Images images);
    Images updateImages(Images deleteImages, List<MultipartFile> storeImages);
}

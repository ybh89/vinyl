package com.hansung.vinyl.news.domain.service;

import com.hansung.vinyl.news.domain.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ImageStore {
    private static final String ORIGINAL_IMAGE_PREFIX = "original-";
    private static final String THUMBNAIL_IMAGE_PREFIX = "thumbnail-";

    @Value("${vinyl.file.directory}")
    private String fileDirectory;

    public List<Image> storeImages(List<MultipartFile> multipartFiles) throws IOException {
        List<Image> storeImages = new ArrayList<>();
        int seq = 1;
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeImages.add(storeImage(multipartFile, seq++));
            }
        }
        return storeImages;
    }

    public Image storeImage(MultipartFile multipartFile, int seq) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalImageName = multipartFile.getOriginalFilename();
        String storeImageName = createStoreImageName(originalImageName);
        storeOriginalImage(multipartFile, storeImageName);
        storeThumbnailImage(multipartFile, storeImageName);

        return Image.builder()
                .storeName(storeImageName)
                .uploadName(originalImageName)
                .seq(seq)
                .build();
    }

    private void storeThumbnailImage(MultipartFile multipartFile, String storeImageName) throws IOException {
        String originalStoreImageName = THUMBNAIL_IMAGE_PREFIX + storeImageName;
        store(multipartFile, originalStoreImageName);
    }

    private void storeOriginalImage(MultipartFile multipartFile, String storeImageName) throws IOException {
        String originalStoreImageName = ORIGINAL_IMAGE_PREFIX + storeImageName;
        store(multipartFile, originalStoreImageName);
    }

    private void store(MultipartFile multipartFile, String originalStoreImageName) throws IOException {
        multipartFile.transferTo(new File(getFullPath(originalStoreImageName)));
    }

    private String getFullPath(String imageName) {
        return fileDirectory + imageName;
    }

    private String createStoreImageName(String originalImageName) {
        String extension = extractExtension(originalImageName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + extension;
    }

    private String extractExtension(String originalImageName) {
        int pos = originalImageName.lastIndexOf(".");
        return originalImageName.substring(pos + 1);
    }
}

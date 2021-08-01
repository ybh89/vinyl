package com.hansung.vinyl.news.domain.service;

import com.hansung.vinyl.common.exception.CannotStoreImageFileException;
import com.hansung.vinyl.news.domain.Image;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ImageStore {
    public static final String ORIGINAL_IMAGE_PREFIX = "original-";
    public static final String THUMBNAIL_IMAGE_PREFIX = "thumbnail-";
    public static final double THUMBNAIL_RATIO = 3;

    @Value("${vinyl.file.directory}")
    private String fileDirectory;

    public List<Image> storeImages(List<MultipartFile> multipartFiles) {
        List<Image> storeImages = new ArrayList<>();
        int seq = 1;
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeImages.add(storeImage(multipartFile, seq++));
            }
        }
        return storeImages;
    }

    public Image storeImage(MultipartFile multipartFile, int seq) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String uploadImageName = multipartFile.getOriginalFilename();
        String storeImageName = createStoreImageName(uploadImageName);
        File originalImage = new File(getFullPath(ORIGINAL_IMAGE_PREFIX + storeImageName));
        File thumbnailImage = new File(getFullPath(THUMBNAIL_IMAGE_PREFIX + storeImageName));

        storeOriginalImage(multipartFile, originalImage);
        storeThumbnailImage(multipartFile, originalImage, thumbnailImage);

        return Image.builder()
                .storeName(storeImageName)
                .uploadName(uploadImageName)
                .seq(seq)
                .build();
    }

    private void storeThumbnailImage(MultipartFile multipartFile, File originalImage, File thumbnailImage) {
        try {
            BufferedImage bufferedOriginalImage = ImageIO.read(originalImage);
            int width = (int) (bufferedOriginalImage.getWidth() / THUMBNAIL_RATIO);
            int height = (int) (bufferedOriginalImage.getHeight() / THUMBNAIL_RATIO);

            Thumbnails.of(originalImage)
                    .size(width, height)
                    .toFile(thumbnailImage);

            store(multipartFile, thumbnailImage);
        } catch (IOException exception) {
            throw new CannotStoreImageFileException();
        }
    }

    private void storeOriginalImage(MultipartFile multipartFile, File originalImage) {
        store(multipartFile, originalImage);
    }

    private void store(MultipartFile multipartFile, File file) {
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new CannotStoreImageFileException();
        }
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

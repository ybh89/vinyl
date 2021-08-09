package com.hansung.vinyl.news.domain.service;

import com.hansung.vinyl.common.exception.CannotStoreImageFileException;
import com.hansung.vinyl.news.domain.Image;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.aspectj.util.FileUtil;
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

@Slf4j
@Component
public class ImageStore {
    public static final String ORIGINAL_IMAGE_PREFIX = "original-";
    public static final String THUMBNAIL_IMAGE_PREFIX = "thumbnail-";
    public static final double THUMBNAIL_RATIO = 3;
    public static final String EXTENSION_DELIMITER = ".";

    @Value("${vinyl.file.directory}")
    private String fileDirectory;

    public List<Image> storeImages(List<MultipartFile> multipartFiles) {
        List<Image> storeImages = new ArrayList<>();
        int seq = 1;
        for (MultipartFile multipartFile : multipartFiles) {
            seq = addStoreImage(storeImages, seq, multipartFile);
        }
        return storeImages;
    }

    private int addStoreImage(List<Image> storeImages, int seq, MultipartFile multipartFile) {
        if (!multipartFile.isEmpty()) {
            storeImages.add(storeImage(multipartFile, seq++));
        }
        return seq;
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
        storeThumbnailImage(originalImage, thumbnailImage);

        return Image.builder()
                .storeName(storeImageName)
                .uploadName(uploadImageName)
                .seq(seq)
                .build();
    }

    private void storeThumbnailImage(File originalImage, File thumbnailImage) {
        try {
            BufferedImage bufferedOriginalImage = ImageIO.read(originalImage);
            int width = (int) (bufferedOriginalImage.getWidth() / THUMBNAIL_RATIO);
            int height = (int) (bufferedOriginalImage.getHeight() / THUMBNAIL_RATIO);

            Thumbnails.of(originalImage)
                    .size(width, height)
                    .toFile(thumbnailImage);
        } catch (IOException exception) {
            throw new CannotStoreImageFileException(exception);
        }
    }

    private void storeOriginalImage(MultipartFile multipartFile, File originalImage) {
        store(multipartFile, originalImage);
    }

    private void store(MultipartFile multipartFile, File file) {
        try {
            multipartFile.transferTo(file);
        } catch (IOException exception) {
            throw new CannotStoreImageFileException(exception);
        }
    }

    public String getFullPath(String imageName) {
        return fileDirectory + imageName;
    }

    private String createStoreImageName(String originalImageName) {
        String extension = extractExtension(originalImageName);
        String uuid = UUID.randomUUID().toString();
        return uuid + EXTENSION_DELIMITER + extension;
    }

    private String extractExtension(String originalImageName) {
        int pos = originalImageName.lastIndexOf(EXTENSION_DELIMITER);
        return originalImageName.substring(pos + 1);
    }

    public byte[] getMainThumbnailImage(Image image) {
        try {
            return FileUtil.readAsByteArray(new File(fileDirectory + THUMBNAIL_IMAGE_PREFIX +
                    image.getStoreName()));
        } catch (IOException exception) {
            log.error("이미지 파일 읽기에 실패하였습니다.", exception);
            return null;
        }
    }

    public void deleteImages(List<Image> images) {
        images.forEach(image -> {
            deleteImage(ORIGINAL_IMAGE_PREFIX + image.getStoreName());
            deleteImage(THUMBNAIL_IMAGE_PREFIX + image.getStoreName());
        });
    }

    private void deleteImage(String storeImageName) {
        FileUtil.deleteContents(new File(fileDirectory + storeImageName));
    }

    public List<Image> updateImages(List<Image> deleteImages, List<MultipartFile> storeImages) {
        deleteImages(deleteImages);
        return storeImages(storeImages);
    }

    public String getFileDirectory() {
        return fileDirectory;
    }
}

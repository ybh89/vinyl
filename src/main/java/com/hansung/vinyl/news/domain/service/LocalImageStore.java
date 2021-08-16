package com.hansung.vinyl.news.domain.service;

import com.hansung.vinyl.common.exception.file.CannotStoreImageFileException;
import com.hansung.vinyl.common.exception.file.NotSupportedFileExtensionException;
import com.hansung.vinyl.news.domain.Image;
import com.hansung.vinyl.news.domain.ImageExtension;
import com.hansung.vinyl.news.domain.Images;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.hansung.vinyl.news.domain.Image.EXTENSION_DELIMITER;

@Slf4j
@Profile({"test", "local"})
@Component
public class LocalImageStore implements ImageStore {
    @Value("${vinyl.file.directory}")
    private String fileDirectory;

    public Images uploadImages(List<MultipartFile> multipartFiles) {
        Images storeImages = new Images();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeImages.add(uploadImage(multipartFile));
            }
        }
        return storeImages;
    }

    public Image uploadImage(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String uploadImageName = multipartFile.getOriginalFilename();
        String storeImageName = createStoreImageName(uploadImageName);
        File originalImage = new File(getImageUrl(ORIGINAL_IMAGE_PREFIX + storeImageName));
        File thumbnailImage = new File(getImageUrl(THUMBNAIL_IMAGE_PREFIX + storeImageName));

        storeOriginalImage(multipartFile, originalImage);
        storeThumbnailImage(originalImage, thumbnailImage);

        return new Image(storeImageName, uploadImageName);
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
            throw new CannotStoreImageFileException(exception, thumbnailImage.getName(), fileDirectory);
        }
    }

    private void storeOriginalImage(MultipartFile multipartFile, File originalImage) {
        store(multipartFile, originalImage);
    }

    private void store(MultipartFile multipartFile, File file) {
        try {
            multipartFile.transferTo(file);
        } catch (IOException exception) {
            throw new CannotStoreImageFileException(exception, file.getName(), fileDirectory);
        }
    }

    public String getImageUrl(String imageName) {
        return fileDirectory + imageName;
    }

    private String createStoreImageName(String originalImageName) {
        String extension = extractExtension(originalImageName);
        validateExtension(originalImageName, extension);
        String uuid = UUID.randomUUID().toString();
        return uuid + EXTENSION_DELIMITER + extension;
    }

    private void validateExtension(String originalImageName, String extension) {
        try {
            ImageExtension.valueOf(extension.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new NotSupportedFileExtensionException(originalImageName, fileDirectory);
        }
    }

    private String extractExtension(String originalImageName) {
        int pos = originalImageName.lastIndexOf(EXTENSION_DELIMITER);
        return originalImageName.substring(pos + 1);
    }

    public void deleteImages(Images images) {
        images.value().forEach(image -> {
            deleteImage(ORIGINAL_IMAGE_PREFIX + image.getStoreName());
            deleteImage(THUMBNAIL_IMAGE_PREFIX + image.getStoreName());
        });
    }

    private void deleteImage(String storeImageName) {
        FileUtil.deleteContents(new File(fileDirectory + storeImageName));
    }

    public Images updateImages(Images deleteImages, List<MultipartFile> storeImages) {
        deleteImages(deleteImages);
        return uploadImages(storeImages);
    }

    public String getFileDirectory() {
        return fileDirectory;
    }
}

package com.hansung.vinyl.news.domain.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.hansung.vinyl.common.exception.file.CannotStoreImageFileException;
import com.hansung.vinyl.common.exception.file.NotSupportedFileExtensionException;
import com.hansung.vinyl.news.domain.Image;
import com.hansung.vinyl.news.domain.ImageExtension;
import com.hansung.vinyl.news.domain.Images;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static com.hansung.vinyl.news.domain.Image.EXTENSION_DELIMITER;

@RequiredArgsConstructor
@Profile("prod")
@Component
public class S3ImageStore implements ImageStore {
    public static final double THUMBNAIL_RATIO = 3;

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public Images uploadImages(List<MultipartFile> multipartFiles) {
        Images storeImages = new Images();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeImages.add(uploadImage(multipartFile));
            }
        }
        return storeImages;
    }

    @Override
    public String getImageUrl(String imageName) {
        return String.valueOf(amazonS3.getUrl(bucket, imageName));
    }

    @Override
    public void deleteImages(Images images) {
        images.value().forEach(image -> {
            deleteImage(ORIGINAL_IMAGE_PREFIX + image.getStoreName());
            deleteImage(THUMBNAIL_IMAGE_PREFIX + image.getStoreName());
        });
    }

    @Override
    public Images updateImages(Images deleteImages, List<MultipartFile> storeImages) {
        deleteImages(deleteImages);
        return uploadImages(storeImages);
    }

    public Image uploadImage(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String uploadImageName = multipartFile.getOriginalFilename();
        String storeImageName = createStoreImageName(uploadImageName);
        storeOriginalImage(multipartFile, storeImageName);
        storeThumbnailImage(multipartFile, storeImageName);
        return new Image(storeImageName, uploadImageName);
    }

    private void storeThumbnailImage(MultipartFile multipartFile, String storeImageName) {
        String thumbnailImageName = THUMBNAIL_IMAGE_PREFIX + storeImageName;
        try {
            BufferedImage thumbnailBufferedImage = makeThumbnailBufferedImage(multipartFile);
            ByteArrayOutputStream thumbnailByteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(thumbnailBufferedImage, extractExtension(storeImageName), thumbnailByteArrayOutputStream);
            ObjectMetadata objectMetadata = setThumbnailObjectMetadata(multipartFile, thumbnailByteArrayOutputStream);
            InputStream thumbnailImageInputStream = new ByteArrayInputStream(thumbnailByteArrayOutputStream.toByteArray());
            amazonS3.putObject(new PutObjectRequest(bucket, thumbnailImageName, thumbnailImageInputStream,
                    objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException ioException) {
            throw new CannotStoreImageFileException(ioException, thumbnailImageName, "");
        }
    }

    private ObjectMetadata setThumbnailObjectMetadata(MultipartFile multipartFile,
                                                      ByteArrayOutputStream thumbnailByteArrayOutputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(thumbnailByteArrayOutputStream.size());
        return objectMetadata;
    }

    private BufferedImage makeThumbnailBufferedImage(MultipartFile multipartFile) throws IOException {
        BufferedImage bufferedOriginalImage = ImageIO.read(multipartFile.getInputStream());
        int width = (int) (bufferedOriginalImage.getWidth() / THUMBNAIL_RATIO);
        int height = (int) (bufferedOriginalImage.getHeight() / THUMBNAIL_RATIO);
        BufferedImage thumbnailBufferedImage = Thumbnails.of(bufferedOriginalImage)
                .size(width, height)
                .asBufferedImage();
        return thumbnailBufferedImage;
    }

    private void storeOriginalImage(MultipartFile multipartFile, String storeImageName) {
        String originalImageName = ORIGINAL_IMAGE_PREFIX + storeImageName;
        try {
            ObjectMetadata objectMetadata = setOriginalObjectMetadata(multipartFile);
            amazonS3.putObject(new PutObjectRequest(bucket, originalImageName, multipartFile.getInputStream(),
                    objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException ioException) {
            throw new CannotStoreImageFileException(ioException, originalImageName, "");
        }
    }

    private ObjectMetadata setOriginalObjectMetadata(MultipartFile multipartFile) throws IOException {
        byte[] imageBytes = IOUtils.toByteArray(multipartFile.getInputStream());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(imageBytes.length);
        return objectMetadata;
    }

    private void deleteImage(String storeImageName) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, storeImageName);
        amazonS3.deleteObject(deleteObjectRequest);
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
            throw new NotSupportedFileExtensionException(originalImageName, "");
        }
    }

    private String extractExtension(String imageName) {
        int pos = imageName.lastIndexOf(EXTENSION_DELIMITER);
        return imageName.substring(pos + 1);
    }
}

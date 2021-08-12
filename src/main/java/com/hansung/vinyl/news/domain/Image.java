package com.hansung.vinyl.news.domain;

import com.google.common.base.Strings;
import com.hansung.vinyl.common.exception.file.NotSupportedFileExtensionException;
import com.hansung.vinyl.common.exception.validate.BlankException;
import lombok.*;

import javax.persistence.*;

import java.util.Arrays;
import java.util.Objects;

import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Image {
    public static final String EXTENSION_DELIMITER = ".";

    @Column(nullable = false, length = 50)
    private String storeName;
    @Column(nullable = false, length = 30)
    private String uploadName;

    public Image(String storeName, String uploadName) {
        validate(storeName, uploadName);
        this.storeName = storeName;
        this.uploadName = uploadName;
    }

    private void validate(String storeName, String uploadName) {
        if (Strings.isNullOrEmpty(storeName) || storeName.isBlank()) {
            throw new BlankException("storeName", storeName, getClass().getName());
        }

        if (Strings.isNullOrEmpty(uploadName) || uploadName.isBlank()) {
            throw new BlankException("uploadName", uploadName, getClass().getName());
        }
        validateExtension(storeName);
        validateExtension(uploadName);
    }

    private void validateExtension(String imageName) {
        String extension = extractExtension(imageName);
        try {
            ImageExtension.valueOf(extension.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new NotSupportedFileExtensionException(imageName, null);
        }
    }

    private String extractExtension(String imageName) {
        int pos = imageName.lastIndexOf(EXTENSION_DELIMITER);
        return imageName.substring(pos + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(getStoreName(), image.getStoreName()) && Objects.equals(getUploadName(), image.getUploadName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStoreName(), getUploadName());
    }
}

package com.hansung.vinyl.news.domain;

import com.google.common.base.Strings;
import com.hansung.vinyl.common.exception.file.NotSupportedFileExtensionException;
import com.hansung.vinyl.common.exception.validate.BlankException;
import lombok.*;

import javax.persistence.*;

import java.util.Arrays;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static lombok.AccessLevel.*;

@Access(FIELD)
@EqualsAndHashCode
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
}

package com.hansung.vinyl.news.ui;

import com.hansung.vinyl.common.exception.file.NoSuchFileException;
import com.hansung.vinyl.news.domain.service.LocalImageStore;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RequestMapping("/images")
@RequiredArgsConstructor
@RestController
public class ImageController {
    private final LocalImageStore localImageStore;

    @GetMapping("/{imageName}")
    public Resource image(@PathVariable String imageName) {
        try {
            return new UrlResource("file:" + localImageStore.getImageUrl(imageName));
        } catch (MalformedURLException e) {
            throw new NoSuchFileException(imageName, localImageStore.getFileDirectory());
        }
    }
}

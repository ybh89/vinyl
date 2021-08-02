package com.hansung.vinyl.news.application;

import com.hansung.vinyl.common.exception.NoSuchDataException;
import com.hansung.vinyl.news.domain.Image;
import com.hansung.vinyl.news.domain.News;
import com.hansung.vinyl.news.domain.NewsRepository;
import com.hansung.vinyl.news.domain.Price;
import com.hansung.vinyl.news.domain.service.ImageStore;
import com.hansung.vinyl.news.dto.NewsListResponse;
import com.hansung.vinyl.news.dto.NewsRequest;
import com.hansung.vinyl.news.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final ImageStore imageStore;

    public NewsResponse create(NewsRequest newsRequest) {
        List<Image> images = imageStore.storeImages(newsRequest.getImages());
        byte[] mainThumbnailImage = imageStore.getMainThumbnailImage(images.get(0));

        News news = News.builder()
                .title(newsRequest.getTitle())
                .content(newsRequest.getContent())
                .sourceUrl(newsRequest.getSourceUrl())
                .releaseDate(newsRequest.getReleaseDate())
                .price(new Price(newsRequest.getPrice(), newsRequest.getPriceType()))
                .images(images)
                .build();

        News saveNews = newsRepository.save(news);
        return NewsResponse.of(saveNews, mainThumbnailImage);
    }

    @Transactional(readOnly = true)
    public NewsResponse find(Long newsId) {
        News news = findNewsByIdAndDeletedFalse(newsId);
        byte[] mainThumbnailImage = imageStore.getMainThumbnailImage(news.getImages().get(0));
        return NewsResponse.of(news, mainThumbnailImage);
    }

    private News findNewsByIdAndDeletedFalse(Long newsId) {
        return newsRepository.findByIdAndDeletedFalse(newsId).orElseThrow(() ->
                new NoSuchDataException("newsId", String.valueOf(newsId), getClass().getName()));
    }

    @Transactional(readOnly = true)
    public Slice<NewsListResponse> list(Pageable pageable) {
        Slice<News> newsPage = newsRepository.findAllByDeletedFalse(pageable);
        return newsPage.map(news -> {
            byte[] mainThumbnailImage = imageStore.getMainThumbnailImage(news.getImages().get(0));
            return NewsListResponse.of(news, mainThumbnailImage);
        });
    }

    public NewsResponse update(Long newsId, NewsRequest newsRequest) {
        News news = findNewsByIdAndDeletedFalse(newsId);
        News updateNews = newsRequest.toNews();
        imageStore.deleteImages(news.getImages());
        List<Image> updateImages = imageStore.storeImages(newsRequest.getImages());
        byte[] mainThumbnailImage = imageStore.getMainThumbnailImage(updateImages.get(0));
        news.update(updateNews, updateImages);
        return NewsResponse.of(news, mainThumbnailImage);
    }

    public void delete(Long newsId) {
        News news = findNewsByIdAndDeletedFalse(newsId);
        imageStore.deleteImages(news.getImages());
        news.delete();
    }
}

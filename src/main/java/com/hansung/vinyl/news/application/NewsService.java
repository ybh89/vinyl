package com.hansung.vinyl.news.application;

import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.common.exception.AuthorizationException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final ImageStore imageStore;

    @Value("${vinyl.init-data.super.role}")
    private String superRole;

    public NewsResponse create(NewsRequest newsRequest) {
        List<Image> images = imageStore.storeImages(newsRequest.getImages());
        byte[] mainThumbnailImage = imageStore.getMainThumbnailImage(images.get(0));
        News saveNews = newsRepository.save(buildNews(newsRequest, images));
        return NewsResponse.of(saveNews, mainThumbnailImage);
    }

    private News buildNews(NewsRequest newsRequest, List<Image> images) {
        return News.builder()
                .title(newsRequest.getTitle())
                .brand(newsRequest.getBrand())
                .content(newsRequest.getContent())
                .sourceUrl(newsRequest.getSourceUrl())
                .releaseDate(newsRequest.getReleaseDate())
                .price(new Price(newsRequest.getPrice(), newsRequest.getPriceType()))
                .topic(newsRequest.getTopic())
                .images(images)
                .build();
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

    public NewsResponse update(User user, Long newsId, NewsRequest newsRequest) {
        News news = findNewsByIdAndDeletedFalse(newsId);
        validateAuthorization(user, news);

        List<Image> updateImages = imageStore.updateImages(news.getImages(), newsRequest.getImages());
        byte[] mainThumbnailImage = imageStore.getMainThumbnailImage(updateImages.get(0));

        news.update(newsRequest.toNews(), updateImages);
        return NewsResponse.of(news, mainThumbnailImage);
    }

    private void validateAuthorization(User user, News news) {
        if (isNoneMatchRoleSuper(user)) {
            validateWriter(user, news);
        }
    }

    private boolean isNoneMatchRoleSuper(User user) {
        return user.getAuthorities().stream().noneMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equals(superRole));
    }

    private void validateWriter(User user, News news) {
        if (!news.getCreatedBy().equals(user.getAccountId())) {
            throw new AuthorizationException();
        }
    }

    public void delete(User user, Long newsId) {
        News news = findNewsByIdAndDeletedFalse(newsId);
        validateAuthorization(user, news);
        imageStore.deleteImages(news.getImages());
        news.delete();
    }

    public List<News> findByReleaseDate(long dDay) {
        LocalDateTime targetDate = LocalDateTime.now().plusDays(dDay);
        LocalDateTime nextDate = targetDate.plusDays(1);
        return newsRepository.findAllByReleaseDateBetweenAndDeletedFalse(targetDate, nextDate);
    }
}

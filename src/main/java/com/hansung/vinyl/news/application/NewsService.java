package com.hansung.vinyl.news.application;

import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.common.exception.AuthorizationException;
import com.hansung.vinyl.common.exception.data.NoSuchDataException;
import com.hansung.vinyl.member.domain.Member;
import com.hansung.vinyl.member.domain.MemberRepository;
import com.hansung.vinyl.news.domain.*;
import com.hansung.vinyl.news.domain.service.ImageStore;
import com.hansung.vinyl.news.domain.service.SubscribeManager;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final ImageStore imageStore;
    private final SubscribeManager subscribeManager;
    private final MemberRepository memberRepository;

    @Value("${vinyl.init-data.super.role}")
    private String superRole;

    public NewsResponse create(NewsRequest newsRequest) {
        Images images = imageStore.storeImages(newsRequest.getImages());
        byte[] mainThumbnailImage = imageStore.getThumbnailImageByte(images.getMainImage());
        Catalog catalog = buildCatalog(newsRequest);
        Post post = buildPost(newsRequest, images);
        News saveNews = newsRepository.save(new News(catalog, post));
        return NewsResponse.of(saveNews, mainThumbnailImage);
    }

    private Post buildPost(NewsRequest newsRequest, Images images) {
        return Post.builder()
                .title(newsRequest.getTitle())
                .content(newsRequest.getContent())
                .images(images)
                .topic(newsRequest.getTopic())
                .build();
    }

    private Catalog buildCatalog(NewsRequest newsRequest) {
        return Catalog.builder()
                .name(newsRequest.getCatalogName())
                .brand(newsRequest.getBrand())
                .price(new Price(newsRequest.getPrice(), newsRequest.getPriceType()))
                .releaseDate(newsRequest.getReleaseDate())
                .sourceUrl(new Url(newsRequest.getSourceUrl()))
                .build();
    }

    @Transactional(readOnly = true)
    public NewsResponse find(Long newsId) {
        News news = findNewsByIdAndDeletedFalse(newsId);
        byte[] mainThumbnailImage = imageStore.getThumbnailImageByte(news.getMainImage());
        return NewsResponse.of(news, mainThumbnailImage);
    }

    private News findNewsByIdAndDeletedFalse(Long newsId) {
        return newsRepository.findByIdAndPostDeletedFalse(newsId).orElseThrow(() ->
                new NoSuchDataException("newsId", String.valueOf(newsId), getClass().getName()));
    }

    @Transactional(readOnly = true)
    public Slice<NewsListResponse> list(Pageable pageable) {
        Slice<News> newsPage = newsRepository.findAllByPostDeletedFalse(pageable);
        return newsPage.map(news -> {
            byte[] mainThumbnailImage = imageStore.getThumbnailImageByte(news.getMainImage());
            return NewsListResponse.of(news, mainThumbnailImage);
        });
    }

    @Transactional(readOnly = true)
    public List<NewsListResponse> list(List<Long> newsIds) {
        List<News> newsList = newsRepository.findAllById(newsIds);
        return newsList.stream()
                .map(news -> {
                    byte[] mainThumbnailImage = imageStore.getThumbnailImageByte(news.getMainImage());
                    return NewsListResponse.of(news, mainThumbnailImage);})
                .collect(Collectors.toList());
    }

    public NewsResponse update(User user, Long newsId, NewsRequest newsRequest) {
        News news = findNewsByIdAndDeletedFalse(newsId);
        validateAuthorization(user, news);

        Images updateImages = imageStore.updateImages(news.getImages(), newsRequest.getImages());
        byte[] mainThumbnailImage = imageStore.getThumbnailImageByte(updateImages.getMainImage());

        News updateNews = newsRequest.toNews();
        updateNews.updateImages(updateImages);
        news.update(updateNews);

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
        return newsRepository.findAllByCatalogReleaseDateBetweenAndPostDeletedFalse(targetDate, nextDate);
    }

    public void subscribe(User user, Long newsId) {
        Member member = findMemberById(user.getAccountId());
        News news = findNewsByIdAndDeletedFalse(newsId);
        subscribeManager.subscribe(member, news);
    }

    public void unsubscribe(User user, Long newsId) {
        Member member = findMemberById(user.getAccountId());
        News news = findNewsByIdAndDeletedFalse(newsId);
        subscribeManager.unsubscribe(member, news);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchDataException("accountId", id, getClass().getName()));
    }
}

package com.hansung.vinyl.news.application;

import com.hansung.vinyl.common.exception.NoSuchDataException;
import com.hansung.vinyl.member.domain.Member;
import com.hansung.vinyl.member.domain.MemberRepository;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final MemberRepository memberRepository;
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
        return NewsResponse.of(saveNews, null, mainThumbnailImage);
    }

    @Transactional(readOnly = true)
    public NewsResponse find(Long newsId) {
        News news = findNewsById(newsId);
        Member writer = findMemberById(news.getCreatedBy());
        byte[] mainThumbnailImage = imageStore.getMainThumbnailImage(news.getImages().get(0));
        return NewsResponse.of(news, writer, mainThumbnailImage);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() ->
                new NoSuchDataException("createdBy", String.valueOf(id), getClass().getName()));
    }

    private News findNewsById(Long newsId) {
        return newsRepository.findById(newsId).orElseThrow(() ->
                new NoSuchDataException("newsId", String.valueOf(newsId), getClass().getName()));
    }

    @Transactional(readOnly = true)
    public Slice<NewsListResponse> list(Pageable pageable) {
        Slice<News> newsPage = newsRepository.findAll(pageable);
        return newsPage.map(news -> {
            byte[] mainThumbnailImage = imageStore.getMainThumbnailImage(news.getImages().get(0));
            return NewsListResponse.of(news, mainThumbnailImage);
        });
    }
}

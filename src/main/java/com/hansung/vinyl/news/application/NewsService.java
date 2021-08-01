package com.hansung.vinyl.news.application;

import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.news.domain.Image;
import com.hansung.vinyl.news.domain.News;
import com.hansung.vinyl.news.domain.NewsRepository;
import com.hansung.vinyl.news.domain.Price;
import com.hansung.vinyl.news.domain.service.ImageStore;
import com.hansung.vinyl.news.dto.NewsRequest;
import com.hansung.vinyl.news.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final ImageStore imageStore;

    public NewsResponse create(User user, NewsRequest newsRequest) {
        List<Image> images = imageStore.storeImages(newsRequest.getImages());

        News news = News.builder()
                .writer(user.getAccountId())
                .title(newsRequest.getTitle())
                .content(newsRequest.getContent())
                .releaseDate(newsRequest.getReleaseDate())
                .price(new Price(newsRequest.getPrice(), newsRequest.getPriceType()))
                .images(images)
                .build();

        News saveNews = newsRepository.save(news);
        return NewsResponse.of(saveNews);
    }
}

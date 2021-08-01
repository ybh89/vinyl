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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final ImageStore imageStore;

    public NewsResponse create(User user, NewsRequest newsRequest) {
        AtomicInteger seq = new AtomicInteger(1);

        List<Image> images = newsRequest.getImages().stream()
                .map(multipartFile -> Image.builder()
                        .name(multipartFile.getName())
                        .seq(seq.getAndIncrement())
                        .build())
                .collect(Collectors.toList());

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

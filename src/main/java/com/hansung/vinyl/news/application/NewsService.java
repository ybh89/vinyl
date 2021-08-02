package com.hansung.vinyl.news.application;

import com.hansung.vinyl.common.exception.NoSuchDataException;
import com.hansung.vinyl.member.domain.Member;
import com.hansung.vinyl.member.domain.MemberRepository;
import com.hansung.vinyl.news.domain.Image;
import com.hansung.vinyl.news.domain.News;
import com.hansung.vinyl.news.domain.NewsRepository;
import com.hansung.vinyl.news.domain.Price;
import com.hansung.vinyl.news.domain.service.ImageStore;
import com.hansung.vinyl.news.dto.NewsRequest;
import com.hansung.vinyl.news.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final MemberRepository memberRepository;
    private final ImageStore imageStore;

    public NewsResponse create(Principal principal, NewsRequest newsRequest) {
        Member member = findMemberByEmail(principal.getName());
        List<Image> images = imageStore.storeImages(newsRequest.getImages());
        byte[] mainThumbnailImage = imageStore.getMainThumbnailImage(images.get(0));

        News news = News.builder()
                .writer(member.getId())
                .title(newsRequest.getTitle())
                .content(newsRequest.getContent())
                .sourceUrl(newsRequest.getSourceUrl())
                .releaseDate(newsRequest.getReleaseDate())
                .price(new Price(newsRequest.getPrice(), newsRequest.getPriceType()))
                .images(images)
                .build();

        News saveNews = newsRepository.save(news);
        return NewsResponse.of(saveNews, member, mainThumbnailImage);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchDataException("email", email, getClass().getName()));
    }
}

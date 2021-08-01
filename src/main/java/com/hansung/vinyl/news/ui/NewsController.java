package com.hansung.vinyl.news.ui;

import com.hansung.vinyl.account.domain.AuthenticationPrincipal;
import com.hansung.vinyl.account.domain.LoginMember;
import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.news.application.NewsService;
import com.hansung.vinyl.news.dto.NewsRequest;
import com.hansung.vinyl.news.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/news")
@RestController
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<NewsResponse> create(@AuthenticationPrincipal User user, NewsRequest newsRequest) {
        NewsResponse newsResponse = newsService.create(user, newsRequest);
        return ResponseEntity.created(URI.create("/news/" + newsResponse.getId())).build();
    }
}
package com.hansung.vinyl.news.ui;

import com.hansung.vinyl.account.domain.AuthenticationPrincipal;
import com.hansung.vinyl.account.domain.LoginMember;
import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.news.application.NewsService;
import com.hansung.vinyl.news.dto.NewsRequest;
import com.hansung.vinyl.news.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/news")
@RestController
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<NewsResponse> create(Principal principal, NewsRequest newsRequest) {
        NewsResponse newsResponse = newsService.create(principal, newsRequest);
        return ResponseEntity.created(URI.create("/news/" + newsResponse.getId())).body(newsResponse);
    }

    @GetMapping("/{newsId}")
    public ResponseEntity<NewsResponse> news(@PathVariable Long newsId) {
        NewsResponse newsResponse = newsService.find(newsId);
        return ResponseEntity.ok(newsResponse);
    }
}

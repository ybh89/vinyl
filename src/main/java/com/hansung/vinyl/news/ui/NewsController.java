package com.hansung.vinyl.news.ui;

import com.hansung.vinyl.account.domain.AuthenticationPrincipal;
import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.news.application.NewsService;
import com.hansung.vinyl.news.dto.NewsListResponse;
import com.hansung.vinyl.news.dto.NewsRequest;
import com.hansung.vinyl.news.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class NewsController {
    private final NewsService newsService;

    @PostMapping("/v1/news")
    public ResponseEntity<NewsResponse> create(@ModelAttribute NewsRequest newsRequest) {
        NewsResponse newsResponse = newsService.create(newsRequest);
        return ResponseEntity.created(URI.create("v1/news/" + newsResponse.getId())).body(newsResponse);
    }

    @GetMapping("/v1/news/{newsId}")
    public ResponseEntity<NewsResponse> news(@PathVariable Long newsId) {
        NewsResponse newsResponse = newsService.find(newsId);
        return ResponseEntity.ok(newsResponse);
    }

    @GetMapping("/v1/news")
    public ResponseEntity<Slice<NewsListResponse>> list(@PageableDefault(size = 20, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Slice<NewsListResponse> newsListResponses = newsService.list(pageable);
        return ResponseEntity.ok(newsListResponses);
    }

    @PutMapping("/v1/news/{newsId}")
    public ResponseEntity<NewsResponse> update(@AuthenticationPrincipal User user, @PathVariable Long newsId,
                                               @ModelAttribute NewsRequest newsRequest) {
        NewsResponse newsResponse = newsService.update(user, newsId, newsRequest);
        return ResponseEntity.ok(newsResponse);
    }

    @DeleteMapping("/v1/news/{newsId}")
    public ResponseEntity delete(@AuthenticationPrincipal User user, @PathVariable Long newsId) {
        newsService.delete(user, newsId);
        return ResponseEntity.noContent().build();
    }
}

package com.hansung.vinyl.news.ui;

import com.hansung.vinyl.account.domain.AuthenticationPrincipal;
import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.news.application.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class SubscribeController {
    private final NewsService newsService;

    @PutMapping("/v1/subscribes/{newsId}")
    public ResponseEntity subscribe(@AuthenticationPrincipal User user, @PathVariable Long newsId) {
        newsService.subscribe(user, newsId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/v1/subscribes/{newsId}")
    public ResponseEntity unsubscribe(@AuthenticationPrincipal User user, @PathVariable Long newsId) {
        newsService.unsubscribe(user, newsId);
        return ResponseEntity.noContent().build();
    }
}

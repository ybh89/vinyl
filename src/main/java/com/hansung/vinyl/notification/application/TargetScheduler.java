package com.hansung.vinyl.notification.application;

import com.hansung.vinyl.news.application.NewsService;
import com.hansung.vinyl.news.domain.News;
import com.hansung.vinyl.notification.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Component
public class TargetScheduler {
    private final NewsService newsService;
    private final NotificationService notificationService;

    /**
     * 매일 18시에 실행
     */
    @Scheduled(cron = "0 0 18 * * *")
    public void findTargetNewsBeforeThreeDays() {
        List<News> targets = newsService.findByReleaseDate(3);
        sendMessage(targets);
    }

    /**
     * 매일 8시에 실행
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void findTargetNewsDDay() {
        List<News> targets = newsService.findByReleaseDate(0);
        sendMessage(targets);
    }

    private void sendMessage(List<News> targets) {
        targets.forEach(news -> {
                try {
                    notificationService.sendMessageByTopic(NotificationRequest.builder()
                        .title(news.getPost().getTitle())
                        .body("여기에 뭐라고 적을까")
                        .imageURL("https://cdn.pixabay.com/photo/2014/06/03/19/38/road-sign-361514_960_720.png")
                        .build());
                } catch (ExecutionException exception) {
                    log.error("메시지 보내기 실패", exception);
                } catch (InterruptedException exception) {
                    log.error("메시지 보내기 실패", exception);
                }
            }
        );
    }
}

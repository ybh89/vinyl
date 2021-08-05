package com.hansung.vinyl.news.domain.service;

import com.hansung.vinyl.member.domain.Member;
import com.hansung.vinyl.news.domain.News;
import org.springframework.stereotype.Component;

@Component
public class SubscribeManager {
    public void subscribe(Member member, News news) {
        member.subscribe(news.getId());
        news.plusSubscribeCount();
    }

    public void unsubscribe(Member member, News news) {
        member.unsubscribe(news.getId());
        news.minusSubscribeCount();
    }
}

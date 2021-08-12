package com.hansung.vinyl.member.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PROTECTED;

@Access(FIELD)
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Subscribes {
    @ElementCollection
    @CollectionTable(name = "subscribe", joinColumns = @JoinColumn(name = "member_id"))
    private List<Subscribe> subscribes = new ArrayList<>();

    public Subscribes(List<Subscribe> subscribes) {
        this.subscribes = subscribes;
    }

    public void subscribe(Long newsId) {
        Subscribe subscribe = new Subscribe(newsId);
        if (!subscribes.contains(subscribe)) {
            subscribes.add(subscribe);
        }
    }

    public void unsubscribe(Long newsId) {
        subscribes.remove(new Subscribe(newsId));
    }

    public List<Long> getSubscribes() {
        return subscribes.stream()
                .map(Subscribe::getNewsId)
                .collect(Collectors.toList());
    }
}

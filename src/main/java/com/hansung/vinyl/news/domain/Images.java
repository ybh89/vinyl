package com.hansung.vinyl.news.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.AccessType.FIELD;

@Access(FIELD)
@Embeddable
public class Images {
    @ElementCollection
    @CollectionTable(name = "image", joinColumns = @JoinColumn(name = "news_id"))
    @OrderColumn(name = "seq")
    private List<Image> images;

    public Images() {
        this.images = new ArrayList<>();
    }

    public void add(Image image) {
        if (!images.contains(image)) {
            images.add(image);
        }
    }

    public List<Image> value() {
        return images;
    }
}

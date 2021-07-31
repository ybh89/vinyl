package com.hansung.vinyl.post.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Price {
    @Column(length = 20)
    private String price;
    @Column
    @Enumerated(EnumType.STRING)
    private PriceType priceType;
}

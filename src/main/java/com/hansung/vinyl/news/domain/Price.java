package com.hansung.vinyl.news.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Access(FIELD)
@Embeddable
public class Price {
    @Column(length = 20)
    private String price;
    @Column
    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    public Price(String price, PriceType priceType) {
        this.price = price;
        this.priceType = priceType;
    }

    public String value() {
        return price;
    }
}

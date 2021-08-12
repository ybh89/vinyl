package com.hansung.vinyl.news.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import java.time.LocalDateTime;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Access(FIELD)
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Catalog {
    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String brand;

    @Embedded
    private Price price;

    @Embedded
    private Url sourceUrl;

    @Column
    private LocalDateTime releaseDate;

    @Builder
    public Catalog(String name, String brand, Price price, Url sourceUrl, LocalDateTime releaseDate) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.sourceUrl = sourceUrl;
        this.releaseDate = releaseDate;
    }
}

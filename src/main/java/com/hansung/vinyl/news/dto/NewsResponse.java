package com.hansung.vinyl.news.dto;

import com.hansung.vinyl.member.dto.MemberResponse;
import com.hansung.vinyl.news.domain.Image;
import com.hansung.vinyl.news.domain.News;
import com.hansung.vinyl.news.domain.Price;
import com.hansung.vinyl.news.domain.PriceType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class NewsResponse {
    private Long id;
    private MemberResponse writer;
    private String title;
    private String content;
    private String sourceUrl;
    private LocalDateTime releaseDate;
    private String price;
    private PriceType priceType;
    private byte[] mainThumbnailImage;
    private List<Image> images = new ArrayList<>();

    public static NewsResponse of(News saveNews) {

        return null;
    }
}

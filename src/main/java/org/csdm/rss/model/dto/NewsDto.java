package org.csdm.rss.model.dto;


import lombok.*;
import org.csdm.rss.model.News;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NewsDto {

    private String title;
    private String description;
    private String uri;
    private LocalDateTime publishDate;
    private String imageUrl;

    public NewsDto(News news) {
        this.title = news.getTitle();
        this.description = news.getDescription();
        this.uri = news.getUri();
        this.publishDate = news.getPublishDate();
        this.imageUrl = news.getImageUrl();
    }
}

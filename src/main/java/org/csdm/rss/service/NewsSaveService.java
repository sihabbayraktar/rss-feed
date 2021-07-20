package org.csdm.rss.service;


import org.csdm.rss.model.News;
import org.csdm.rss.model.dto.NewsDto;
import org.csdm.rss.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class NewsSaveService {

    @Autowired
    private NewsRepository newsRepository;

    public void saveNewsAcc2Feed(List<String> urisFromRssFeed, Map<String, NewsDto> newsFromDb, Map<String, NewsDto> newsFromFeed) {
        List<News> news = urisFromRssFeed.stream().filter(s -> {
            return newsFromDb.get(s) == null;
        }).map(s -> {
                    NewsDto feedDto = newsFromFeed.get(s);
                    return News.builder().description(feedDto.getDescription()).uri(feedDto.getUri())
                            .title(feedDto.getTitle()).imageUrl(feedDto.getImageUrl()).publishDate(feedDto.getPublishDate())
                            .build();
                }).collect(Collectors.toList());

        newsRepository.saveAll(news);
    }

}

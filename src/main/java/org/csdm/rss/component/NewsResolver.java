package org.csdm.rss.component;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.csdm.rss.model.News;
import org.csdm.rss.model.dto.NewsDto;
import org.csdm.rss.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsResolver implements GraphQLQueryResolver {

    @Autowired
    private NewsRepository newsRepository;

    public NewsDto news(String uri) {
        News byUri = newsRepository.findByUri(uri);
        return new NewsDto(byUri);
    }

    public List<NewsDto> allNews() {
        List<News> all = newsRepository.findAll();
        return all.stream().map(news -> {
            return new NewsDto(news);
        }).sorted(Comparator.comparing(NewsDto::getPublishDate).reversed()).collect(Collectors.toList());
    }


}

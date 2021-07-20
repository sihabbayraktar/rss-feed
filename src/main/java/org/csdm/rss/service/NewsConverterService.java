package org.csdm.rss.service;

import com.rometools.rome.feed.synd.SyndEntry;
import org.csdm.rss.model.News;
import org.csdm.rss.model.dto.NewsDto;
import org.csdm.rss.repository.NewsRepository;
import org.csdm.rss.util.IParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NewsConverterService {

    @Autowired
    private NewsRepository newsRepository;


    //Map-Struct cozumunu dusun.
    public Map<String, NewsDto> createMapFromNews(List<NewsDto> newsFromDb) {
        return newsFromDb.stream().collect(Collectors.toMap(NewsDto::getUri, Function.identity()));
    }

    public List<NewsDto> rss2Dto(List<SyndEntry> syndEntries) {
        return syndEntries.stream().limit(IParameter.RSS_FEED_SIZE).map(syndEntry -> {
            return NewsDto.builder().title(syndEntry.getTitle())
                    .description(syndEntry.getDescription().getValue())
                    .publishDate(syndEntry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .imageUrl(syndEntry.getEnclosures().get(0).getUrl())
                    .uri(syndEntry.getUri())
                    .build();
        }).collect(Collectors.toList());
    }

    public List<NewsDto> newsFromDbAndConvert2Dto(List<String> uris) {
        return uris.stream().filter(s -> {
            return newsRepository.findByUri(s) != null;
        }).map(s -> {
            News byUri = newsRepository.findByUri(s);
            NewsDto newsDto = NewsDto.builder()
                    .uri(byUri.getUri())
                    .publishDate(byUri.getPublishDate())
                    .imageUrl(byUri.getImageUrl())
                    .description(byUri.getDescription())
                    .title(byUri.getDescription()).build();
            return newsDto;
        }).collect(Collectors.toList());
    }

    public List<String> getAllUris(List<SyndEntry> syndEntries) {
        return syndEntries.stream()
                .limit(IParameter.RSS_FEED_SIZE)
                .map(SyndEntry::getUri)
                .collect(Collectors.toList());
    }


}

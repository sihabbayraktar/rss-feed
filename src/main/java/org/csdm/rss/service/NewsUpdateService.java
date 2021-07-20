package org.csdm.rss.service;

import org.csdm.rss.model.News;
import org.csdm.rss.model.dto.NewsDto;
import org.csdm.rss.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
public class NewsUpdateService {

    @Autowired
    private NewsRepository newsRepository;


    public void updateNewsAcc2Feed(List<String> urisFromRssFeed, Map<String, NewsDto> newsFromDb, Map<String, NewsDto> newsFromFeed) {
        urisFromRssFeed.stream().filter(s->newsFromDb.containsKey(s)).forEach(update(newsFromDb, newsFromFeed));
    }

    private Consumer<? super String> update(Map<String, NewsDto> newsFromDb, Map<String, NewsDto> newsFromFeed) {
        return uri -> updateNews(newsFromDb.get(uri),newsFromFeed.get(uri));
    }


    private void updateNews(NewsDto newsFromDb, NewsDto newsFromFeed) {
        News news2beUpdated = newsRepository.findByUri(newsFromDb.getUri());

        checkTitle(newsFromDb.getTitle(), newsFromFeed.getTitle(), news2beUpdated);
        checkDescription(newsFromDb.getDescription(), newsFromFeed.getDescription(), news2beUpdated);
        checkPublishDate(newsFromDb.getPublishDate(), newsFromFeed.getPublishDate(), news2beUpdated);
        checkImageUrl(newsFromDb.getImageUrl(), newsFromFeed.getImageUrl(), news2beUpdated);
    }

    private void checkPublishDate(LocalDateTime publishDate, LocalDateTime publishDate1, News news2beUpdated) {
        if(!publishDate.equals(publishDate1)) {
            news2beUpdated.setPublishDate(publishDate1);
        }
    }

    private void checkDescription(String description, String description1, News news2beUpdated) {
        if(!description.equals(description1)) {
            news2beUpdated.setDescription(description1);
        }
    }

    private void checkTitle(String title, String title1, News news2beUpdated) {
        if(!title.equals(title1)) {
            news2beUpdated.setTitle(title1);
        }
    }

    private void checkImageUrl(String imageUrl, String imageUrl1, News news2beUpdated) {
        if(!imageUrl.equals(imageUrl1)) {
            news2beUpdated.setImageUrl(imageUrl1);
        }
    }

}

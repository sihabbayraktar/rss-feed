package org.csdm.rss;


import org.csdm.rss.model.News;
import org.csdm.rss.model.dto.NewsDto;
import org.csdm.rss.repository.NewsRepository;
import org.csdm.rss.service.NewsConverterService;
import org.csdm.rss.service.NewsSaveService;
import org.csdm.rss.service.NewsUpdateService;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class GeneralTest {

    @Autowired
    private NewsSaveService saveService;

    @Autowired
    private NewsUpdateService updateService;

    @Autowired
    private NewsConverterService newsConverterService;

    @Autowired
    private NewsRepository newsRepository;

    @Test
    void testSave() {

        save();

        List<News> all = newsRepository.findAll();
        System.out.println("size = "  + all.size());
        Assert.assertFalse(all.size()==13);

    }

    @Test
    void testUpdate() {

        save();
        List<String> uris = List.of("https://nos.nl/l/2389698-1", "https://nos.nl/l/2389695-1");
        List<NewsDto> newsFromDb = newsConverterService.newsFromDbAndConvert2Dto(uris);
        Map<String, NewsDto> mapFromNewsDb = newsConverterService.createMapFromNews(newsFromDb);


        Map<String, NewsDto> dummyNews = new HashMap<>();
        dummyNews.put("https://nos.nl/l/2389698-1",
                dummyNewsCreate("desc-changed", "title-changed", "https://nos.nl/l/2389698-1",
                        "url-1", LocalDateTime.now()));

        dummyNews.put("https://nos.nl/l/2389695-1",
                dummyNewsCreate("desc-changed2", "title-changed2", "https://nos.nl/l/2389695-1",
                        "url-2", LocalDateTime.now()));

        updateService.updateNewsAcc2Feed(uris, mapFromNewsDb, dummyNews);

        News byUri = newsRepository.findByUri("https://nos.nl/l/2389695-1");
        String desc = byUri.getDescription();
        Assert.assertEquals(desc, "desc-changed2");
        String title = byUri.getTitle();
        Assert.assertEquals(title, "title-changed2");
    }

    private NewsDto dummyNewsCreate(String desc, String title,
                              String uri, String url, LocalDateTime publishDate){
        return NewsDto.builder().title(title).description(desc)
                .uri(uri).imageUrl(url).publishDate(publishDate).build();

    }

    private void save() {
        NewsDto newsDto1 = dummyNewsCreate("desc-1", "title-1", "https://nos.nl/l/2389698-1",
                "url-1", LocalDateTime.now());

        NewsDto newsDto2 = dummyNewsCreate("desc-2", "title-2", "https://nos.nl/l/2389695-1",
                "url-2", LocalDateTime.now());

        NewsDto newsDto3 = dummyNewsCreate("desc-3", "title-3", "https://nos.nl/l/2389682-1",
                "url-3", LocalDateTime.now());

        List<String> uris = List.of("https://nos.nl/l/2389698-1", "https://nos.nl/l/2389695-1", "https://nos.nl/l/2389682-1");



        Map<String, NewsDto> dummyNews = newsConverterService.createMapFromNews(List.of(newsDto1, newsDto2, newsDto3));
        saveService.saveNewsAcc2Feed(uris, new HashMap<String, NewsDto>(), dummyNews);
    }



}

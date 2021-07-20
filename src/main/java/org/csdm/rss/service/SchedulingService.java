package org.csdm.rss.service;

import com.rometools.rome.feed.synd.SyndEntry;
import lombok.Getter;
import lombok.SneakyThrows;
import org.csdm.rss.model.dto.NewsDto;
import org.csdm.rss.util.IParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

@EnableScheduling
@Service
public class SchedulingService {


    @Autowired
    private NewsSaveService saveService;

    @Autowired
    private NewsUpdateService updateService;

    @Autowired
    private RssFetchService fetchService;

    @Autowired
    private NewsConverterService newsConverterService;

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Getter
    private final BlockingQueue<SyndEntry> queue = new LinkedBlockingQueue<>(100);

    @Transactional
    @Scheduled(fixedRate = IParameter.TIME_INTERVAL)
    public void fetch() {

        List<SyndEntry> syndEntries = fetchService.fetch();

        if(syndEntries.isEmpty()){
            return;
        }
        getQueue().addAll(syndEntries);
        System.err.println("1 - size = " + getQueue().size());
        //executeTask();
        /*List<String> uris = newsConverterService.getAllUris(syndEntries);

        List<NewsDto> newsFromDb = newsConverterService.newsFromDbAndConvert2Dto(uris);
        List<NewsDto> newsFromRss = newsConverterService.rss2Dto(syndEntries);

        Map<String, NewsDto> mapFromNewsDb = newsConverterService.createMapFromNews(newsFromDb);
        Map<String, NewsDto> mapFromNewsRss = newsConverterService.createMapFromNews(newsFromRss);

        updateService.updateNewsAcc2Feed(uris, mapFromNewsDb, mapFromNewsRss);
        saveService.saveNewsAcc2Feed(uris, mapFromNewsDb, mapFromNewsRss);
    */
    }
/*
    Runnable consumerTask = () -> {

        while(true) {
            List<SyndEntry> list = new ArrayList<>();
            queue.stream().forEach(syndEntry1 -> {
                list.add(syndEntry1);
            });
            List<String> uris = newsConverterService.getAllUris(list);

            List<NewsDto> newsFromDb = newsConverterService.newsFromDbAndConvert2Dto(uris);
            List<NewsDto> newsFromRss = newsConverterService.rss2Dto(list);

            Map<String, NewsDto> mapFromNewsDb = newsConverterService.createMapFromNews(newsFromDb);
            Map<String, NewsDto> mapFromNewsRss = newsConverterService.createMapFromNews(newsFromRss);

            updateService.updateNewsAcc2Feed(uris, mapFromNewsDb, mapFromNewsRss);
            saveService.saveNewsAcc2Feed(uris, mapFromNewsDb, mapFromNewsRss);
        }
    };
*/
    public void executeTask() {
        for(int i = 0; i < 2; i++) {
            executorService.execute(new Consumer());
        }
    }

    class Consumer implements Runnable {

        private List<SyndEntry> list;

        public Consumer() {
            list = new ArrayList<>();
        }

        @SneakyThrows
        @Override
        public void run() {

            while(true) {

                while(getQueue().size() != 0) {
                    list.add(getQueue().take());
                }

                List<String> uris = newsConverterService.getAllUris(list);

                List<NewsDto> newsFromDb = newsConverterService.newsFromDbAndConvert2Dto(uris);
                List<NewsDto> newsFromRss = newsConverterService.rss2Dto(list);

                Map<String, NewsDto> mapFromNewsDb = newsConverterService.createMapFromNews(newsFromDb);
                Map<String, NewsDto> mapFromNewsRss = newsConverterService.createMapFromNews(newsFromRss);

                updateService.updateNewsAcc2Feed(uris, mapFromNewsDb, mapFromNewsRss);
                saveService.saveNewsAcc2Feed(uris, mapFromNewsDb, mapFromNewsRss);
            }
        }

    }

}



package org.csdm.rss.service;


import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.csdm.rss.util.IParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

@Service
public class RssFetchService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<SyndEntry> fetch()  {
        try{
            URL source = new URL(IParameter.FEED_SOURCE_URL);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(source));
            List<SyndEntry> entries = feed.getEntries();
            entries.sort(Comparator.comparing(SyndEntry::getPublishedDate).reversed());
            return entries;
        }
        catch (Exception e) {
            throw new RuntimeException("Fetch Exception");
        }

    }
}

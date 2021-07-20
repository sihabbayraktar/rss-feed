package org.csdm.rss.repository;

import org.csdm.rss.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    News findByUri(String uri);

}

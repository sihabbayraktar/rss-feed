# rss-feed
This application pools the most recent news as rss feed from the http://feeds.nos.nl/nosjournaal?format=xml.

Technologies:
  * Java 14.0.1
  * Maven 3.6.3
  * Spring Boot 2.5.2
  * Spring Data JPA
  * Spring Web
  * H2
  * Rome
  * GraphQL
  * Lombok

## RUN
 1. Clone the repository
 2. Run `mvn clean install` command in the terminal. 
 3. Run `java -jar target/rss-0.0.1-SNAPSHOT.jar` in the terminal.
 4. Then [graphiql link](http://localhost:8080/graphiql) to query


### Sample Query
```
  allNews {
    uri
    title
    description
    imageUrl
    publishDate
  }
}

{
  news(uri: "https://nos.nl/l/2389853"){
    uri
    title
  }
}

```

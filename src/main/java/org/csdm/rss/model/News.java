package org.csdm.rss.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "NEWS")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "uri", unique = true)
    private String uri;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = 15000)
    private String description;

    @Column(name = "publish_date")
    private LocalDateTime publishDate;

    @Column(name = "image_url")
    private String imageUrl;
}

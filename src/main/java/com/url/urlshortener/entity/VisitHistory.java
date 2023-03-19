package com.url.urlshortener.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class VisitHistory {
    @EmbeddedId
    private VisitHistoryId id;

    @MapsId("shortUrl")
    @ManyToOne
    @JoinColumn(name = "short", referencedColumnName = "short")
    private UrlMap urlMap;

    @Column(name = "browser", length = 10)
    private String browser;

    @Column(name = "location", length = 20)
    private String location;

    @Column(name = "language", length = 2)
    private String language;

    @Column(name = "visited", columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Timestamp visited;
}

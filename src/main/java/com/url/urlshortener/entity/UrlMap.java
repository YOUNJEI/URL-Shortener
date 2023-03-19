package com.url.urlshortener.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class UrlMap {
    @Id
    @Column(name = "origin")
    private String origin;

    @Column(name = "short", nullable = false, length = 7)
    private String shortUrl;

    @Column(name = "created", columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created;
}

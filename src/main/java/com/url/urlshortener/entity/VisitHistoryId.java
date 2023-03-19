package com.url.urlshortener.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class VisitHistoryId implements Serializable {
    @Column(name = "short")
    private String shortUrl;

    private Long id;
}

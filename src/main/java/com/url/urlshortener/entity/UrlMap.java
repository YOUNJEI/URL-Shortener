package com.url.urlshortener.entity;

import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class UrlMap implements Persistable<String> {
    @Id
    @Column(name = "origin", length = 500)
    private String origin;

    @Column(name = "short", nullable = false, length = 7)
    private String shortUrl;

    @Column(name = "created", columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created;

    @Transient
    private boolean isNew = true;

    @Override
    public String getId() { return origin; }

    @Override
    public boolean isNew() { return isNew; }

    @PrePersist
    @PostLoad
    public void markNotNew() { this.isNew = false; }

    @Builder
    public UrlMap(String origin, String shortUrl) {
        this.origin = origin;
        this.shortUrl = shortUrl;
    }
}

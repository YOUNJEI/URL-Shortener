package com.url.urlshortener.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class UrlMap implements Persistable<String> {
    @Id
    @Column(name = "origin")
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
}

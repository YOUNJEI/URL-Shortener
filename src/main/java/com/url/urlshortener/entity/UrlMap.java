package com.url.urlshortener.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class UrlMap implements Persistable<UrlMapId>, Serializable {
    @EmbeddedId
    private UrlMapId id;

    @Column(name = "short", nullable = false, length = 7)
    private String shortUrl;

    @CreationTimestamp
    @Column(name = "created", columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp created;

    @Transient
    private boolean isNew = true;

    @Override
    public UrlMapId getId() { return id; }

    @Override
    public boolean isNew() { return isNew; }

    @PrePersist
    @PostLoad
    public void markNotNew() { this.isNew = false; }

    @Builder
    public UrlMap(UrlMapId urlMapId, String shortUrl) {
        this.id = urlMapId;
        this.shortUrl = shortUrl;
    }
}

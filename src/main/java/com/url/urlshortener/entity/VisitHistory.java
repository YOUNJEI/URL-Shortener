package com.url.urlshortener.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class VisitHistory implements Persistable<VisitHistoryId> {
    @EmbeddedId
    private VisitHistoryId id;

    @Column(name = "browser", length = 10)
    private String browser;

    @Column(name = "location", length = 20)
    private String location;

    @Column(name = "language", length = 2)
    private String language;

    @CreationTimestamp
    @Column(name = "visited", columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp visited;

    @Transient
    private boolean isNew = true;

    @Override
    public VisitHistoryId getId() { return id; }

    @Override
    public boolean isNew() { return isNew; }

    @PrePersist
    @PostLoad
    public void markNotNew() { this.isNew = false; }

    @Builder
    public VisitHistory(VisitHistoryId id, String browser, String location, String language) {
        this.id = id;
        this.browser = browser;
        this.location = location;
        this.language = language;
    }
}

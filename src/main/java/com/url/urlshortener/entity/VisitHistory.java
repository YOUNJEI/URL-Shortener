package com.url.urlshortener.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class VisitHistory {
    @EmbeddedId
    private VisitHistoryId id;

    @Column(name = "browser", length = 10)
    private String browser;

    @Column(name = "location", length = 20)
    private String location;

    @Column(name = "language", length = 2)
    private String language;

    @Column(name = "visited", columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Timestamp visited;
}

package com.url.urlshortener.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class VisitHistoryId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "short", referencedColumnName = "short")
    private UrlMap urlMap;

    private Long id;
}

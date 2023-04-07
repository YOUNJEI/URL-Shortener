package com.url.urlshortener.service;

import com.url.urlshortener.controller.dto.HistoryResponseDto;
import com.url.urlshortener.entity.VisitHistory;
import com.url.urlshortener.repository.VisitHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class VisitHistoryService {
    private final VisitHistoryRepository visitHistoryRepository;

    public List<HistoryResponseDto> getUrlList(HttpServletRequest httpServletRequest) {
        return null;
    }
}

package com.url.urlshortener.controller;

import com.url.urlshortener.service.VisitHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class VisitHistoryController {
    private final VisitHistoryService visitHistoryService;

    @GetMapping("/page/url-list")
    public String urlListPage(Model model) {
        model.addAttribute("urls", visitHistoryService.getUrlList());
        return "url-list";
    }

    @GetMapping("/page/url-list/{short}")
    public String urlDetailPage(Model model, @PathVariable("short") String shortUrl) {
        model.addAttribute("response", visitHistoryService.getDetail(shortUrl));
        return "url-detail";
    }
}

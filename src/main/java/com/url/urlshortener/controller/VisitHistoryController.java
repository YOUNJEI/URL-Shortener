package com.url.urlshortener.controller;

import com.url.urlshortener.service.VisitHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class VisitHistoryController {
    private final VisitHistoryService visitHistoryService;

    @GetMapping("/page/url-list")
    public String urlListPage(Model model) {
        model.addAttribute("urls", visitHistoryService.getUrlList());
        return "url-list";
    }
}

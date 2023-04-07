package com.url.urlshortener.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VisitHistoryController {

    @GetMapping("/page/url-list")
    public String urlListPage() {
        return "url-list";
    }
}

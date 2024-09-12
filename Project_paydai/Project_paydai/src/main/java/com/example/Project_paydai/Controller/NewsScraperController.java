package com.example.Project_paydai.Controller;

import com.example.Project_paydai.Service.NewsScraperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
public class NewsScraperController {

    private final NewsScraperService newsScraperService;
    public NewsScraperController(NewsScraperService newsScraperService) {
    this.newsScraperService = newsScraperService;
    }
    @GetMapping("/search")
    public List<Map<String, String>> searchNews(@RequestParam("query") String query) {
        return newsScraperService.scrapeGoogleNews(query);
    }

}


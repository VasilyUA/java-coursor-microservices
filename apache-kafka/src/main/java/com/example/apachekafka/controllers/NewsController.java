package com.example.apachekafka.controllers;

import com.example.apachekafka.dtos.news.News;
import com.example.apachekafka.kafka.produsers.NewsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    @Autowired
    private NewsProducer newsProducer;

    @PostMapping
    public String createNews(@RequestBody News news) {
        newsProducer.sendNews(news);
        return "News created";
    }

}

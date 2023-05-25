package com.example.apachekafka.services;

import com.example.apachekafka.documents.News;
import com.example.apachekafka.dtos.news.NewsDTO;
import com.example.apachekafka.exeptions.ResourceExistsException;
import com.example.apachekafka.repository.NewsRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewsRepo newsRepo;

    public News createNews(NewsDTO newsDTO) {
        var news = objectMapper.convertValue(newsDTO, News.class);
        if (newsRepo.findNewsByTitle(news.getTitle()) != null) {
            throw new ResourceExistsException("News with title" + news.getTitle() + "already existing");
        }

        return newsRepo.save(news);
    }
}

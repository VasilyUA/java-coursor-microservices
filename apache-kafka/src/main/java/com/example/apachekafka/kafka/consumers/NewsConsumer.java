package com.example.apachekafka.kafka.consumers;

import com.example.apachekafka.dtos.news.NewsDTO;
import com.example.apachekafka.exeptions.ResourceExistsException;
import com.example.apachekafka.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NewsConsumer {
    private static final String TOPIC = "news_topic";
    private static final String groupId  = "group1";

    @Autowired
    private NewsService newsService;

    @KafkaListener(topics = TOPIC, groupId = groupId)
    public void consume(NewsDTO newsDTO) {
        try {
            var news = newsService.createNews(newsDTO);
            System.out.println("consumer1: News with title was created" + news.getTitle());
        } catch (ResourceExistsException e) {
            System.out.println("consumer1: Error -" + e.getMessage());
        }
    }
}

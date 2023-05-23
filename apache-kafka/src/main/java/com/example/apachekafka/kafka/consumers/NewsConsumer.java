package com.example.apachekafka.kafka.consumers;

import com.example.apachekafka.dtos.news.News;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NewsConsumer {
    private static final String TOPIC = "news_topic";
    private static final String groupId  = "group1";
    @KafkaListener(topics = TOPIC, groupId = groupId)
    public void consume(News news){
        System.out.println(news.getTitle());
    }
}

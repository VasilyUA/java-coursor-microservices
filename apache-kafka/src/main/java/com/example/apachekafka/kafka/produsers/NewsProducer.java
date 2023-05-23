package com.example.apachekafka.kafka.produsers;

import com.example.apachekafka.dtos.news.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NewsProducer {
    private static final String TOPIC = "news_topic";

    @Autowired
    private KafkaTemplate<String, News> kafkaTemplate;

    public void sendNews(News news) {
        this.kafkaTemplate.send(TOPIC, news);
    }
}

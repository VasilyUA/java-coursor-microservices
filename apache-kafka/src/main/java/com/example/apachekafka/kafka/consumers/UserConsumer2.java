package com.example.apachekafka.kafka.consumers;

import com.example.apachekafka.dtos.users.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumer2 {
    private static final String TOPIC = "user_topic";
    private static final String groupId  = "group2";
    @KafkaListener(topics = TOPIC, groupId = groupId)
    public void consume(User user){
        System.out.println("Consumer2:" + user.getEmail());
    }
}

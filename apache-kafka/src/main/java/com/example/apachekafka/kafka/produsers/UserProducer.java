package com.example.apachekafka.kafka.produsers;

import com.example.apachekafka.dtos.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducer {
    private static final String TOPIC = "user_topic";

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    public void sendUser(User user) {
        this.kafkaTemplate.send(TOPIC, user);
    }
}

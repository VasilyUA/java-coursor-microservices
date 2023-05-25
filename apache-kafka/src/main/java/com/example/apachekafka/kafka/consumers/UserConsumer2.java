package com.example.apachekafka.kafka.consumers;

import com.example.apachekafka.dtos.users.UserDTO;
import com.example.apachekafka.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumer2 {
    private static final String TOPIC = "user_topic";
    private static final String groupId  = "group2";

    @Autowired
    private UserService userService;

    @KafkaListener(topics = TOPIC, groupId = groupId)
    public void consume(UserDTO userDTO){
        userService.sendMessageToEmail(userDTO);
    }
}

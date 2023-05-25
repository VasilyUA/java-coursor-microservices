package com.example.apachekafka.kafka.produsers;

import com.example.apachekafka.dtos.users.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducer {
    private static final String TOPIC = "user_topic";

    @Autowired
    private KafkaTemplate<String, UserDTO> kafkaTemplate;

    public void sendUser(UserDTO userDTO) {
        this.kafkaTemplate.send(TOPIC, userDTO);
    }
}

package com.example.apachekafka.kafka.consumers;

import com.example.apachekafka.dtos.users.UserDTO;
import com.example.apachekafka.exeptions.ResourceExistsException;
import com.example.apachekafka.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumer1 {
    private static final String TOPIC = "user_topic";
    private static final String groupId  = "group1";

    @Autowired
    private UserService userService;


    @KafkaListener(topics = TOPIC, groupId = groupId)
    public void consume(UserDTO userDTO){
        try {
            var user = userService.createUser(userDTO);
            System.out.println("consumer1: User with email - " + user.getEmail() + "was created");
        } catch (ResourceExistsException e) {
            System.out.println("consumer1: Error - " + e.getMessage());
        }
    }
}

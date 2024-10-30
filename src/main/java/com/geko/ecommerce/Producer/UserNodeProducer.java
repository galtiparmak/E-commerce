package com.geko.ecommerce.Producer;

import com.geko.ecommerce.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserNodeProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public UserNodeProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private static final String CREATE_TOPIC = "user-node-save-topic";
    private static final String DELETE_TOPIC = "user-node-delete-topic";

    public void createUser(String username) {
        try {
            System.out.println("User produced to Kafka: " + username);
            kafkaTemplate.send(CREATE_TOPIC, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String username) {
        try {
            System.out.println("User produced to Kafka: " + username);
            kafkaTemplate.send(DELETE_TOPIC, username);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.geko.ecommerce.Producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public UserProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private static final String CREATE_TOPIC = "user-save-topic";
    private static final String DELETE_TOPIC = "user-delete-topic";
    private static final String GET_ALL_BOUGHT_PRODUCTS_BY_USER_TOPIC = "user-bought-products-topic";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createUser(User user) {
        try {
            System.out.println("User produced to Kafka: " + user);
            String jsonUser = objectMapper.writeValueAsString(user);
            kafkaTemplate.send(CREATE_TOPIC, jsonUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(User user) {
        try {
            System.out.println("User deleted from Kafka: " + user);
            String jsonUser = objectMapper.writeValueAsString(user);
            kafkaTemplate.send(DELETE_TOPIC, jsonUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAllBoughtProductsByUser(String username) {
        try {
            System.out.println("User's bought products requested from Kafka: " + username);
            kafkaTemplate.send(GET_ALL_BOUGHT_PRODUCTS_BY_USER_TOPIC, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

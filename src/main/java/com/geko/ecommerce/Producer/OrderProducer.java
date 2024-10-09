package com.geko.ecommerce.Producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private static final String CREATE_TOPIC = "order-save-topic";
    private static final String DELETE_TOPIC = "order-delete-topic";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createOrder(Order order) {
        try {
            System.out.println("Order produced to Kafka: " + order);
            String jsonOrder = objectMapper.writeValueAsString(order);
            kafkaTemplate.send(CREATE_TOPIC, jsonOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteOrder(Order order) {
        try {
            System.out.println("Order deleted from Kafka: " + order);
            String jsonOrder = objectMapper.writeValueAsString(order);
            kafkaTemplate.send(DELETE_TOPIC, jsonOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

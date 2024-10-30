package com.geko.ecommerce.Producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductNodeProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String CREATE_TOPIC = "product-node-save-topic";
    private static final String DELETE_TOPIC = "product-node-delete-topic";

    @Autowired
    public ProductNodeProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void create(Product product) {
        try {
            System.out.println("Product produced to kafka: " + product.getName());
            String jsonProduct = objectMapper.writeValueAsString(product);
            kafkaTemplate.send(CREATE_TOPIC, jsonProduct);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        try {
            System.out.println("Product produced to kafka: " + id);
            kafkaTemplate.send(DELETE_TOPIC, id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

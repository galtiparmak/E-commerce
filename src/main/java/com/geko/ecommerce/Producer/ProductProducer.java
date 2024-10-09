package com.geko.ecommerce.Producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ProductProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private static final String CREATE_TOPIC = "product-save-topic";
    private static final String DELETE_TOPIC = "product-delete-topic";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createProduct(Product product) {
        try {
            System.out.println("Product produced to Kafka: " + product);
            String jsonProduct = objectMapper.writeValueAsString(product);
            kafkaTemplate.send(CREATE_TOPIC, jsonProduct);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(Product product) {
        try {
            System.out.println("Product deleted from Kafka: " + product);
            String jsonProduct = objectMapper.writeValueAsString(product);
            kafkaTemplate.send(DELETE_TOPIC, jsonProduct);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


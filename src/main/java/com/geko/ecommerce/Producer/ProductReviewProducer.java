package com.geko.ecommerce.Producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.ProductReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductReviewProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ProductReviewProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private static final String CREATE_TOPIC = "product-review-save-topic";
    private static final String DELETE_TOPIC = "product-review-delete-topic";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createReview(ProductReview productReview) {
        try {
            System.out.println("Product review produced to Kafka: " + productReview);
            String jsonProductReview = objectMapper.writeValueAsString(productReview);
            kafkaTemplate.send(CREATE_TOPIC, jsonProductReview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteReview(ProductReview productReview) {
        try {
            System.out.println("Product review deleted from Kafka: " + productReview);
            String jsonProductReview = objectMapper.writeValueAsString(productReview);
            kafkaTemplate.send(DELETE_TOPIC, jsonProductReview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

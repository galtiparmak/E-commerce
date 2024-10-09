package com.geko.ecommerce.Consumer.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.ProductReview;
import com.geko.ecommerce.Repository.mongodb.ProductReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MongoProductReviewConsumer {
    private final ProductReviewRepository productReviewRepository;

    @Autowired
    public MongoProductReviewConsumer(ProductReviewRepository productReviewRepository) {
        this.productReviewRepository = productReviewRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "product-review-save-topic", groupId = "mongo-consumer-group")
    public void save(String productReviewJson) {
        try {
            ProductReview productReview = objectMapper.readValue(productReviewJson, ProductReview.class);
            productReviewRepository.save(productReview);
            System.out.println("Product review saved to MongoDB: " + productReview);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "product-review-delete-topic", groupId = "mongo-consumer-group")
    public void delete(String productReviewJson) {
        try {
            ProductReview productReview = objectMapper.readValue(productReviewJson, ProductReview.class);
            productReviewRepository.delete(productReview);
            System.out.println("Product review deleted from MongoDB: " + productReview);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

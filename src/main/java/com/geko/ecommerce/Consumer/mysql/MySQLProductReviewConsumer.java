package com.geko.ecommerce.Consumer.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.ProductAverage;
import com.geko.ecommerce.Entity.ProductReview;
import com.geko.ecommerce.Repository.mongodb.ProductReviewRepository;
import com.geko.ecommerce.Repository.mysql.ProductAverageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MySQLProductReviewConsumer {
    private final ProductAverageRepository productAverageRepository;

    @Autowired
    public MySQLProductReviewConsumer(ProductAverageRepository productAverageRepository) {
        this.productAverageRepository = productAverageRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "product-review-save-topic", groupId = "mysql-consumer-group")
    public void updateAverage(String productReviewJson) {
        try {
            ProductReview productReview = objectMapper.readValue(productReviewJson, ProductReview.class);
            Optional<ProductAverage> optionalProductAverage = productAverageRepository.findByProductName(productReview.getProductName());
            if (optionalProductAverage.isEmpty()) {
                ProductAverage productAverage = new ProductAverage();
                productAverage.setProductName(productReview.getProductName());
                productAverage.setAverage(productReview.getRating());
                productAverage.setTotalReviews(1);
                productAverageRepository.save(productAverage);
                System.out.println("Product average saved to MySQL: " + productAverage);
            }
            else {
                ProductAverage productAverage = optionalProductAverage.get();
                productAverage.setAverage((productAverage.getAverage() * productAverage.getTotalReviews() + productReview.getRating()) / (productAverage.getTotalReviews() + 1));
                productAverage.setTotalReviews(productAverage.getTotalReviews() + 1);
                productAverageRepository.save(productAverage);
                System.out.println("Product average updated in MySQL: " + productAverage);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

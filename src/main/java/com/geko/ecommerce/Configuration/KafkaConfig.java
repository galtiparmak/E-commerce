package com.geko.ecommerce.Configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;



@Configuration
public class KafkaConfig {

    // Order Topics
    @Bean
    public NewTopic orderSaveTopic() {
        return new NewTopic("order-save-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic orderDeleteTopic() {
        return new NewTopic("order-delete-topic", 2, (short) 1);
    }

    // Product Topics
    @Bean
    public NewTopic productSaveTopic() {
        return new NewTopic("product-save-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic productDeleteTopic() {
        return new NewTopic("product-delete-topic", 2, (short) 1);
    }

    // User Topics
    @Bean
    public NewTopic userSaveTopic() {
        return new NewTopic("user-save-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic userDeleteTopic() {
        return new NewTopic("user-delete-topic", 2, (short) 1);
    }

    // Product Review Topics
    @Bean
    public NewTopic productReviewSaveTopic() {
        return new NewTopic("product-review-create-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic productReviewDeleteTopic() {
        return new NewTopic("product-review-delete-topic", 2, (short) 1);
    }
}

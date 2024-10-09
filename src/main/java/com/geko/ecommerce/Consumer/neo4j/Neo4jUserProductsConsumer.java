package com.geko.ecommerce.Consumer.neo4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.DTO.Product.ProductDTO;
import com.geko.ecommerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Neo4jUserProductsConsumer {
    private final UserService userService;

    @Autowired
    public Neo4jUserProductsConsumer(UserService userService) {
        this.userService = userService;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "user-product-topic", groupId = "neo4j-consumer-group")
    public void consume(String productsJson) {
        try {
            List<ProductDTO> products = objectMapper.readValue(productsJson, List.class);

            userService.handleResponse(products);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

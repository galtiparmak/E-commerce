package com.geko.ecommerce.Consumer.neo4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.Order;
import com.geko.ecommerce.Entity.Product;
import com.geko.ecommerce.Entity.ProductNode;
import com.geko.ecommerce.Entity.UserNode;
import com.geko.ecommerce.Repository.neo4j.ProductNodeRepository;
import com.geko.ecommerce.Repository.neo4j.UserNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Neo4jOrderConsumer {
    private final UserNodeRepository userNodeRepository;
    private final ProductNodeRepository productNodeRepository;

    @Autowired
    public Neo4jOrderConsumer(UserNodeRepository userNodeRepository, ProductNodeRepository productNodeRepository) {
        this.userNodeRepository = userNodeRepository;
        this.productNodeRepository = productNodeRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "order-save-topic", groupId = "neo4j-consumer-group")
    public void consumeOrder(String orderJson) {
        try {
            Order order = objectMapper.readValue(orderJson, Order.class);
            Optional<UserNode> optionalUser = userNodeRepository.findByUsername(order.getUsername());
            if (optionalUser.isEmpty()) {
                throw new RuntimeException("User not found with username: " + order.getUsername());
            }
            UserNode user = optionalUser.get();

            for (String productName : order.getProductNames()) {
                Optional<ProductNode> optionalProduct = productNodeRepository.findByName(productName);
                if (optionalProduct.isEmpty()) {
                    throw new RuntimeException("Product not found: " + productName);
                }
                ProductNode product = optionalProduct.get();
                user.getBoughtProducts().add(product);
            }

            userNodeRepository.save(user);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to process order: " + e.getMessage());
        }
    }
}

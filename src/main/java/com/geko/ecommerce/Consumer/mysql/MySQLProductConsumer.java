package com.geko.ecommerce.Consumer.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.Product;
import com.geko.ecommerce.Repository.mysql.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MySQLProductConsumer {
    private final ProductRepository productRepository;

    @Autowired
    public MySQLProductConsumer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "product-save-topic", groupId = "mysql-consumer-group")
    public void create(String productJson) {
        try {
            Product product = objectMapper.readValue(productJson, Product.class);
            productRepository.save(product);
            System.out.println("Product saved to MySQL: " + product);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "product-delete-topic", groupId = "mysql-consumer-group")
    public void delete(String productJson) {
        try {
            Product product = objectMapper.readValue(productJson, Product.class);
            Optional<Product> productOptional = productRepository.findById(product.getId());
            if (productOptional.isPresent()) {
                productRepository.delete(product);
                System.out.println("Product deleted from MySQL: " + product);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}


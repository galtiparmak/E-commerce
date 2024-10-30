package com.geko.ecommerce.Consumer.neo4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.Product;
import com.geko.ecommerce.Entity.ProductNode;
import com.geko.ecommerce.Repository.neo4j.ProductNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Neo4jProductConsumer {
    private final ProductNodeRepository productNodeRepository;

    @Autowired
    public Neo4jProductConsumer(ProductNodeRepository productNodeRepository) {
        this.productNodeRepository = productNodeRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "product-node-save-topic", groupId = "neo4j-consumer-group")
    public void create(String productJson){
        try {
            Product product = objectMapper.readValue(productJson, Product.class);
            ProductNode productNode = new ProductNode();
            productNode.setName(product.getName());
            productNode.setDescription(product.getDescription());
            productNode.setPrice(product.getPrice());
            productNodeRepository.save(productNode);
            System.out.println("Product node saved to Neo4j repository");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "product-node-delete-topic", groupId = "neo4j-consumer-group")
    public void delete(String id) {
        try {
            productNodeRepository.deleteProductAndRelationships(id);
            System.out.println("Product Node deleted from Neo4j repository");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

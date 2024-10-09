package com.geko.ecommerce.Consumer.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.Product;
import com.geko.ecommerce.Entity.ProductDocument;
import com.geko.ecommerce.Repository.elasticsearch.ProductSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchProductConsumer {
    private final ProductSearchRepository productSearchRepository;

    @Autowired
    public ElasticsearchProductConsumer(ProductSearchRepository productSearchRepository) {
        this.productSearchRepository = productSearchRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "product-save-topic", groupId = "elasticsearch-consumer-group")
    public void create(String productJson) {
        try {
            Product product = objectMapper.readValue(productJson, Product.class);
            ProductDocument productDocument = new ProductDocument();
            productDocument.setName(product.getName());
            productDocument.setPrice(product.getPrice());
            productDocument.setDescription(product.getDescription());

            productSearchRepository.save(productDocument);
            System.out.println("Product indexed to Elasticsearch: " + productDocument);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

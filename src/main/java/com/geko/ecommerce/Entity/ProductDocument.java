package com.geko.ecommerce.Entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDocument {
    @Id
    private String id;
    private String name;
    private String description;
    private double price;
}

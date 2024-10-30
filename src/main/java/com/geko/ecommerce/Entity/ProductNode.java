package com.geko.ecommerce.Entity;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductNode {
    @Id
    private String name;
    private String description;
    private double price;
}

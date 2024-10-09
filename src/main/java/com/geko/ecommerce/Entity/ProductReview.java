package com.geko.ecommerce.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "product_reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReview implements Serializable {
    @Id
    private String id;
    private String productName;
    private String username;
    private String reviewText;
    private double rating;
}

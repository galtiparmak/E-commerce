package com.geko.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_average")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAverage implements Comparable<ProductAverage> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productName;
    private double average;
    private int totalReviews;

    @Override
    public int compareTo(ProductAverage other) {
        return Double.compare(this.average, other.average);
    }
}

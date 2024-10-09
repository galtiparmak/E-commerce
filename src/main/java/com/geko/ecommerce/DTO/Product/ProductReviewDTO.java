package com.geko.ecommerce.DTO.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewDTO {
    private String productId;
    private double avgRating;
}

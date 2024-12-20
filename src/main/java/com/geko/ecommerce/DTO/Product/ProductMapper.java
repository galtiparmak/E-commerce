package com.geko.ecommerce.DTO.Product;

import com.geko.ecommerce.Entity.Product;
import com.geko.ecommerce.Entity.ProductNode;

public class ProductMapper {
    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }

    public static ProductDTO toDTO(ProductNode product) {
        return new ProductDTO(
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }

    public static Product toProduct(ProductDTO productDTO) {
        return Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .build();
    }
}

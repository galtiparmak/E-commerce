package com.geko.ecommerce.Repository.mysql;

import com.geko.ecommerce.Entity.ProductAverage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductAverageRepository extends JpaRepository<ProductAverage, Long> {
    Optional<ProductAverage> findByProductName(String productName);
}

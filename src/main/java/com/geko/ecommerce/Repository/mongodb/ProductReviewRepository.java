package com.geko.ecommerce.Repository.mongodb;

import com.geko.ecommerce.Entity.ProductReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductReviewRepository extends MongoRepository<ProductReview, Long> {
    List<ProductReview> findById(String productId);
}

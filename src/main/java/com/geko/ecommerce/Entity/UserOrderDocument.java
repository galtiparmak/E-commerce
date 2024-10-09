package com.geko.ecommerce.Entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "user_orders")
public class UserOrderDocument {
    @Id
    private String id;
    private String userId;
    private List<String> orderIds;
}

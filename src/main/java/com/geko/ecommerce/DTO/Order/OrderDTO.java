package com.geko.ecommerce.DTO.Order;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String username;
    private List<String> productNames;
    private Date orderDate;
    private Date expectedDeliveryDate;
    private double totalPrice;
}

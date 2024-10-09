package com.geko.ecommerce.DTO.Order;

import com.geko.ecommerce.Entity.Order;

import java.util.Calendar;
import java.util.Date;

public class OrderMapper {
    public static OrderDTO toDTO(Order order) {
        return new OrderDTO(
                order.getUserId(),
                order.getProductIds(),
                order.getOrderDate(),
                order.getExpectedDeliveryDate(),
                order.getTotalPrice()
        );
    }

    public static Order toOrder(OrderDTO orderDTO) {
        return Order.builder()
                .userId(orderDTO.getUserId())
                .productIds(orderDTO.getProductIds())
                .orderDate(new Date())
                .expectedDeliveryDate(getDateAfterDays(3))
                .totalPrice(orderDTO.getTotalPrice())
                .build();
    }

    private static Date getDateAfterDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }
}

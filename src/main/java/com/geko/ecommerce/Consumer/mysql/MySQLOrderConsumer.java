package com.geko.ecommerce.Consumer.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.Order;
import com.geko.ecommerce.Entity.Product;
import com.geko.ecommerce.Repository.mysql.OrderRepository;
import com.geko.ecommerce.Repository.mysql.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MySQLOrderConsumer {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public MySQLOrderConsumer(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "order-save-topic", groupId = "mysql-consumer-group")
    public void create(String orderJson) {
        try {
            Order order = objectMapper.readValue(orderJson, Order.class);
            order.setOrderDate(new Date());
            order.setExpectedDeliveryDate(getDateAfterThreeDays());
            order.setTotalPrice(calculateTotalPrice(order.getProductNames()));
            orderRepository.save(order);
            System.out.println("Order saved to MySQL: " + order);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "order-delete-topic", groupId = "mysql-consumer-group")
    public void delete(String orderJson) {
        try {
            Order order = objectMapper.readValue(orderJson, Order.class);
            orderRepository.delete(order);
            System.out.println("Order deleted from MySQL: " + order);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Date getDateAfterThreeDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        return calendar.getTime();
    }

    private double calculateTotalPrice(List<String> productNames) {
        double totalPrice = 0;
        for (String product : productNames) {
            Optional<Product> optionalProduct = productRepository.findByName(product);
            if (optionalProduct.isEmpty()) {
                throw new RuntimeException("Product not found with name: " + product);
            }
            double price = optionalProduct.get().getPrice();
            totalPrice += price;
        }
        return totalPrice;
    }
}

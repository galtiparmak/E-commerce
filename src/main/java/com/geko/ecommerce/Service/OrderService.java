package com.geko.ecommerce.Service;

import com.geko.ecommerce.DTO.Order.OrderDTO;
import com.geko.ecommerce.DTO.Order.OrderMapper;
import com.geko.ecommerce.Entity.Order;
import com.geko.ecommerce.Producer.OrderProducer;
import com.geko.ecommerce.Repository.mysql.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;


    @Autowired
    public OrderService(OrderRepository orderRepository, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.orderProducer = orderProducer;
    }

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (Order order : orders) {
            orderDTOS.add(OrderMapper.toDTO(order));
        }
        return orderDTOS;
    }

    public OrderDTO getOrderById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        return OrderMapper.toDTO(optionalOrder.get());
    }

    public boolean createOrder(Order order) {
        if (order != null) {
            try {
                orderProducer.createOrder(order);
                return true;
            } catch (RuntimeException e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean deleteOrder(Long id) {
        try {
            Optional<Order> optionalOrder = orderRepository.findById(id);
            if (optionalOrder.isEmpty()) {
                throw new RuntimeException("Order not found with id: " + id);
            }
            Order order = optionalOrder.get();
            orderProducer.deleteOrder(order);
            return true;
        } catch (RuntimeException e) {
            System.err.println("An error occurred: " + e.getMessage());
            return false;
        }
    }

}

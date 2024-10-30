package com.geko.ecommerce.Controller;

import com.geko.ecommerce.DTO.Order.OrderDTO;
import com.geko.ecommerce.Entity.Order;
import com.geko.ecommerce.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<OrderDTO>> getOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/get")
    public ResponseEntity<OrderDTO> getOrder(@RequestParam Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        if (orderService.createOrder(order)) {
            return ResponseEntity.ok("Order is created successfully");
        }
        else {
            return ResponseEntity.badRequest().body("Order creation is failed");
        }
    }
}

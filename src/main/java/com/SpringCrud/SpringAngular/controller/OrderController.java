package com.SpringCrud.SpringAngular.controller;

import com.SpringCrud.SpringAngular.model.Order;
import com.SpringCrud.SpringAngular.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "https://strong-praline-d86c79.netlify.app/")
@RestController
@RequestMapping("/api/orders") // Base path
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // Save order
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    // Get all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get orders by userId (optional)
    @GetMapping("/filter")
    public List<Order> getOrders(@RequestParam(required = false) String userId) {
        if (userId != null && !userId.isEmpty()) {
            return orderRepository.findByUserId(userId);
        }
        return orderRepository.findAll();
    }

    // ✅ FIXED: Update status for order by ID
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");

        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        Order order = optionalOrder.get();
        order.setStatus(newStatus); // Ensure your Order entity has this field
        orderRepository.save(order);

        return ResponseEntity.ok().body("Status updated successfully");
    }
}

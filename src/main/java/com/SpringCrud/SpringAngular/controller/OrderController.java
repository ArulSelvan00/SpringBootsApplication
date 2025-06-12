package com.SpringCrud.SpringAngular.controller;

import com.SpringCrud.SpringAngular.model.Order;
import com.SpringCrud.SpringAngular.repository.OrderRepository;
import com.SpringCrud.SpringAngular.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Placed");
        Order savedOrder = orderRepository.save(order);

        // ✅ Send confirmation email
        if (savedOrder.getEmail() != null && !savedOrder.getEmail().isEmpty()) {
            String subject = "Order Confirmation - #" + savedOrder.getId();
            String body = "Hi " + savedOrder.getCustomerName() + ",\n\n" +
                    "Your order for \"" + savedOrder.getProductName() + "\" has been successfully placed.\n" +
                    "Order ID: " + savedOrder.getId() + "\n" +
                    "Quantity: " + savedOrder.getQuantity() + "\n" +
                    "Amount: ₹" + savedOrder.getAmount() + "\n\n" +
                    "Thank you for shopping with us!\n\n- Sree Sivam Sarees";
            emailService.sendOrderConfirmationEmail(savedOrder.getEmail(), subject, body);
        }

        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/filter")
    public List<Order> getOrders(@RequestParam(required = false) String userId) {
        if (userId != null && !userId.isEmpty()) {
            return orderRepository.findByUserId(userId);
        }
        return orderRepository.findAll();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");

        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.status(404).body("Order not found");
        }

        Order order = optionalOrder.get();
        order.setStatus(newStatus);
        orderRepository.save(order);

        return ResponseEntity.ok("Status updated successfully");
    }
}

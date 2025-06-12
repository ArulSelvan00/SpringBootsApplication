package com.SpringCrud.SpringAngular.controller;

import com.SpringCrud.SpringAngular.model.Order;
import com.SpringCrud.SpringAngular.repository.OrderRepository;
import com.SpringCrud.SpringAngular.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;  // ✅ Inject EmailService

    // ✅ Save order & send email
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        Order savedOrder = orderRepository.save(order);

        // ✅ Send confirmation email
        if (order.getEmail() != null && !order.getEmail().isEmpty()) {
            String subject = "Order Confirmation - " + savedOrder.getProductName();
            String body = "Dear " + savedOrder.getCustomerName() + ",\n\n"
                    + "Thank you for your purchase!\n\n"
                    + "Order Details:\n"
                    + "Product: " + savedOrder.getProductName() + "\n"
                    + "Quantity: " + savedOrder.getQuantity() + "\n"
                    + "Total Amount: ₹" + savedOrder.getAmount() + "\n"
                    + "Delivery Address: " + savedOrder.getFullAddress() + "\n\n"
                    + "We will notify you once your order is shipped.\n\n"
                    + "Best regards,\n"
                    + "Your Food Delivery Team";

            try {
                emailService.sendOrderEmail(order.getEmail(), subject, body);
            } catch (Exception e) {
                e.printStackTrace();  // Log or handle email error
            }
        }

        return ResponseEntity.ok(savedOrder);
    }

    // Get all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Filter by user ID
    @GetMapping("/filter")
    public List<Order> getOrders(@RequestParam(required = false) String userId) {
        if (userId != null && !userId.isEmpty()) {
            return orderRepository.findByUserId(userId);
        }
        return orderRepository.findAll();
    }

    // Update order status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");

        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        Order order = optionalOrder.get();
        order.setStatus(newStatus);
        orderRepository.save(order);

        return ResponseEntity.ok().body("Status updated successfully");
    }
}

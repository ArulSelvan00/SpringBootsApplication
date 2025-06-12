package com.SpringCrud.SpringAngular.controller;

import com.SpringCrud.SpringAngular.model.Order;
import com.SpringCrud.SpringAngular.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "https://strong-praline-d86c79.netlify.app")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JavaMailSender mailSender;

    // ✅ Save order + send confirmation email
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        Order savedOrder = orderRepository.save(order);
        sendOrderConfirmationEmail(savedOrder); // ✅ Send email to customer + admin
        return ResponseEntity.ok(savedOrder);
    }

    // ✅ Email method
    private void sendOrderConfirmationEmail(Order order) {
        String subject = "🧾 Order Confirmation - FoodDelivery";
        String message = """
            ✅ THANK YOU FOR YOUR ORDER ✅

            🧾 Product: %s
            🔢 Quantity: %d
            💰 Amount Paid: ₹%.2f
            🆔 Payment ID: %s

            👤 Name: %s
            📧 Email: %s
            📱 Contact: %s
            🏠 Address: %s

            👤 User ID: %s

            🙏 We will deliver your order soon!
            """.formatted(
            order.getProductName(),
            order.getQuantity(),
            order.getAmount(),
            order.getPaymentId(),
            order.getCustomerName(),
            order.getEmail(),
            order.getContact(),
            order.getFullAddress(),
            order.getUserId()
        );

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(order.getEmail()); // ✅ Send to customer
        email.setCc("arulkarthiksasi@gmail.com"); // ✅ Copy to you (admin) – change this
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }

    // ✅ Get all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ✅ Filter by userId
    @GetMapping("/filter")
    public List<Order> getOrders(@RequestParam(required = false) String userId) {
        if (userId != null && !userId.isEmpty()) {
            return orderRepository.findByUserId(userId);
        }
        return orderRepository.findAll();
    }

    // ✅ Update order status
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

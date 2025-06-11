package com.SpringCrud.SpringAngular.repository;

import com.SpringCrud.SpringAngular.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 🔍 Find orders by customer name
    List<Order> findByCustomerName(String customerName);

    // 🔍 Find orders by email
    List<Order> findByEmail(String email);

    // 🔍 Find orders by contact number
    List<Order> findByContact(String contact);

    // 🔍 Optional: Find orders by product name
    List<Order> findByProductName(String productName);
    List<Order> findByUserId(String userId);
}

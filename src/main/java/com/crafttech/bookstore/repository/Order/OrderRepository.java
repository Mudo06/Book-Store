package com.crafttech.bookstore.repository.Order;

import com.crafttech.bookstore.model.User;
import com.crafttech.bookstore.model.Order.Order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
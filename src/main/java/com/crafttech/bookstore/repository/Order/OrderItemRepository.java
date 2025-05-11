package com.crafttech.bookstore.repository.Order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crafttech.bookstore.model.Order.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
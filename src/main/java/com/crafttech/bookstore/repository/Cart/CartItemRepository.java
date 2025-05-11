package com.crafttech.bookstore.repository.Cart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crafttech.bookstore.model.Cart.CartItem;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByIdIn(List<Long> ids);
}
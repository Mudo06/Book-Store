package com.crafttech.bookstore.repository.Cart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crafttech.bookstore.model.Cart.ShoppingCart;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUserId(Long userId);
}
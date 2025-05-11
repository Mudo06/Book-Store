package com.crafttech.bookstore.controller;

import com.crafttech.bookstore.dto.Cart.CartItemDTO;
import com.crafttech.bookstore.dto.Cart.ShoppingCartDTO;
import com.crafttech.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart Management", description = "APIs for managing shopping carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/my-cart")
    @Operation(summary = "Get current user's shopping cart")
    public ResponseEntity<ShoppingCartDTO> getMyCart(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(shoppingCartService.getMyCart(token));
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to shopping cart")
    public ResponseEntity<ShoppingCartDTO> addItemToCart(
            @RequestHeader("Authorization") String token,
            @RequestBody CartItemDTO cartItemDTO) {
        return ResponseEntity.ok(shoppingCartService.addItemToCart(token, cartItemDTO));
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Update cart item quantity")
    public ResponseEntity<ShoppingCartDTO> updateCartItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(shoppingCartService.updateCartItem(token, itemId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove item from shopping cart")
    public ResponseEntity<Void> removeItemFromCart(
            @RequestHeader("Authorization") String token,
            @PathVariable Long itemId) {
        shoppingCartService.removeItemFromCart(token, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear shopping cart")
    public ResponseEntity<Void> clearCart(
            @RequestHeader("Authorization") String token) {
        shoppingCartService.clearCart(token);
        return ResponseEntity.noContent().build();
    }
}
package com.crafttech.bookstore.controller;

import com.crafttech.bookstore.dto.Order.CreateOrderRequest;
import com.crafttech.bookstore.dto.Order.OrderDTO;
import com.crafttech.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order from shopping cart")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(token, request));
    }

    @GetMapping
    @Operation(summary = "Get all orders for current user")
    public ResponseEntity<List<OrderDTO>> getUserOrders(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.getUserOrders(token));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order details by ID")
    public ResponseEntity<OrderDTO> getOrderDetails(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(token, orderId));
    }
}
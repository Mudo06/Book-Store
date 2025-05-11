package com.crafttech.bookstore.dto.Cart;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartItemDTO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private double bookPrice;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package com.crafttech.bookstore.dto.Cart;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShoppingCartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package com.crafttech.bookstore.dto.Order;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private int quantity;
    private BigDecimal unitPrice;
}
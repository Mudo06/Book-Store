package com.crafttech.bookstore.model.Order;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.crafttech.bookstore.model.Book;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;
}
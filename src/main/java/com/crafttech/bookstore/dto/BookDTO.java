package com.crafttech.bookstore.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String isbn;
    private double price;
    private int stockQuantity;
    private String description;
    private LocalDate publishedDate;
    private Set<Long> authorIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
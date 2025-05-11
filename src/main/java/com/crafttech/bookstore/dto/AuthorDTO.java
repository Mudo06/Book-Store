package com.crafttech.bookstore.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AuthorDTO {
    private Long id;
    private String name;
    private String biography;
    private Set<Long> bookIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
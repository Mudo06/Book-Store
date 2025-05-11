package com.crafttech.bookstore.dto;

import com.crafttech.bookstore.model.User.Role;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long shoppingCartId;
}
package com.crafttech.bookstore.dto.Jwt;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
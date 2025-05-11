package com.crafttech.bookstore.controller;

import com.crafttech.bookstore.dto.UserDTO;
import com.crafttech.bookstore.dto.Jwt.AuthResponseDTO;
import com.crafttech.bookstore.dto.Jwt.SignInRequest;
import com.crafttech.bookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.getAllUsers(token));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.getUserById(id, token));
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
    }

    @PutMapping
    @Operation(summary = "Update current user")
    public ResponseEntity<UserDTO> updateCurrentUser(
            @RequestBody UserDTO userDTO,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.updateCurrentUser(userDTO, token));
    }

    @PostMapping("admin")
    @Operation(summary = "Create a new admin")
    public ResponseEntity<UserDTO> createAdmin(@RequestBody UserDTO userDTO,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createAdmin(userDTO, token));
    }

    @PutMapping("admin/{id}")
    @Operation(summary = "Update an existing admin")
    public ResponseEntity<UserDTO> updateAdmin(@PathVariable Long id,
            @RequestBody UserDTO userDTO,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.updateAdmin(id, userDTO, token));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        userService.deleteUser(id, token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signin")
    @Operation(summary = "Sign in a user and get JWT token")
    public ResponseEntity<AuthResponseDTO> signIn(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(userService.signIn(signInRequest.getEmail(), signInRequest.getPassword()));
    }
}
package com.crafttech.bookstore.controller;

import com.crafttech.bookstore.dto.AuthorDTO;
import com.crafttech.bookstore.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Author Management", description = "APIs for managing authors")
public class AuthorController {
    private final AuthorService authorService;
    

    @GetMapping
    @Operation(summary = "Get all authors")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID")
    public ResponseEntity<AuthorDTO> getAuthorById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authorService.getAuthorById(id, token));
    }

    @PostMapping
    @Operation(summary = "Create a new author")
    public ResponseEntity<AuthorDTO> createAuthor(
            @RequestBody AuthorDTO authorDTO,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorService.createAuthor(authorDTO, token));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing author")
    public ResponseEntity<AuthorDTO> updateAuthor(
            @PathVariable Long id,
            @RequestBody AuthorDTO authorDTO,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authorService.updateAuthor(id, authorDTO, token));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author")
    public ResponseEntity<Void> deleteAuthor(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        authorService.deleteAuthor(id, token);
        return ResponseEntity.noContent().build();
    }
}
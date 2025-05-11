package com.crafttech.bookstore.controller;

import com.crafttech.bookstore.dto.BookDTO;
import com.crafttech.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Book Management", description = "APIs for managing books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    public ResponseEntity<BookDTO> getBookById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        BookDTO bookDTO = bookService.getBookById(id, token);
        return ResponseEntity.ok(bookDTO);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by title")
    public ResponseEntity<List<BookDTO>> searchBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get books by author")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    @PostMapping
    @Operation(summary = "Create a new book")
    public ResponseEntity<BookDTO> createBook(
            @RequestBody BookDTO bookDTO,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(bookDTO, token));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long id,
            @RequestBody BookDTO bookDTO,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDTO, token));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        bookService.deleteBook(id, token);
        return ResponseEntity.noContent().build();
    }
}
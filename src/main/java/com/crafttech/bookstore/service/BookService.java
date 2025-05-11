package com.crafttech.bookstore.service;

import com.crafttech.bookstore.config.SecurityUtil;
import com.crafttech.bookstore.dto.BookDTO;
import com.crafttech.bookstore.exception.ResourceNotFoundException;
import com.crafttech.bookstore.exception.UnauthorizedException;
import com.crafttech.bookstore.model.Author;
import com.crafttech.bookstore.model.Book;
import com.crafttech.bookstore.model.User;
import com.crafttech.bookstore.model.User.Role;
import com.crafttech.bookstore.repository.AuthorRepository;
import com.crafttech.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final SecurityUtil securityUtil;

    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAllWithAuthors();
        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id, String token) {
        validateAdmin(token);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return convertToDTO(book);
    }

    @Transactional(readOnly = true)
    public List<BookDTO> searchBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitle(title);
        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getBooksByAuthor(Long authorId) {
        List<Book> books = bookRepository.findByAuthorId(authorId);
        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDTO createBook(BookDTO bookDTO, String token) {
        validateAdmin(token);

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setPrice(bookDTO.getPrice());
        book.setStockQuantity(bookDTO.getStockQuantity());
        book.setDescription(bookDTO.getDescription());
        book.setPublishedDate(bookDTO.getPublishedDate());

        if (bookDTO.getAuthorIds() != null) {
            Set<Author> authors = new HashSet<>(authorRepository.findAllById(bookDTO.getAuthorIds()));
            authors.forEach(book::addAuthor);
        }

        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO, String token) {
        validateAdmin(token);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setPrice(bookDTO.getPrice());
        book.setStockQuantity(bookDTO.getStockQuantity());
        book.setDescription(bookDTO.getDescription());
        book.setPublishedDate(bookDTO.getPublishedDate());

        if (bookDTO.getAuthorIds() != null) {
            Set<Author> authors = new HashSet<>(authorRepository.findAllById(bookDTO.getAuthorIds()));
            book.setAuthors(authors);
        }

        Book updatedBook = bookRepository.save(book);
        return convertToDTO(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id, String token) {
        validateAdmin(token);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
    }

    private void validateAdmin(String token) {
        User requester = securityUtil.validateAndGetUserFromToken(token);
        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admins can perform this action");
        }
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPrice(book.getPrice());
        dto.setStockQuantity(book.getStockQuantity());
        dto.setDescription(book.getDescription());
        dto.setPublishedDate(book.getPublishedDate());

        if (Hibernate.isInitialized(book.getAuthors())) {
            dto.setAuthorIds(book.getAuthors().stream()
                    .map(Author::getId)
                    .collect(Collectors.toSet()));
        } else {
            dto.setAuthorIds(new HashSet<>());
        }

        dto.setCreatedAt(book.getCreatedAt());
        dto.setUpdatedAt(book.getUpdatedAt());
        return dto;
    }
}
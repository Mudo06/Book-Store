package com.crafttech.bookstore.service;

import com.crafttech.bookstore.config.SecurityUtil;
import com.crafttech.bookstore.dto.AuthorDTO;
import com.crafttech.bookstore.exception.ResourceNotFoundException;
import com.crafttech.bookstore.exception.UnauthorizedException;
import com.crafttech.bookstore.model.Author;
import com.crafttech.bookstore.model.Book;
import com.crafttech.bookstore.model.User;
import com.crafttech.bookstore.model.User.Role;
import com.crafttech.bookstore.repository.AuthorRepository;
import com.crafttech.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final SecurityUtil securityUtil;

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id, String token) {
        User requester = securityUtil.validateAndGetUserFromToken(token);
        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admins can access author details");
        }

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return convertToDTO(author);
    }

    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO, String token) {
        User requester = securityUtil.validateAndGetUserFromToken(token);
        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admins can create authors");
        }

        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setBiography(authorDTO.getBiography());

        if (authorDTO.getBookIds() != null) {
            Set<Book> books = new HashSet<>(bookRepository.findAllById(authorDTO.getBookIds()));
            books.forEach(author::addBook);
        }

        Author savedAuthor = authorRepository.save(author);
        return convertToDTO(savedAuthor);
    }

    @Transactional
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO, String token) {
        User requester = securityUtil.validateAndGetUserFromToken(token);
        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admins can update authors");
        }

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));

        author.setName(authorDTO.getName());
        author.setBiography(authorDTO.getBiography());

        if (authorDTO.getBookIds() != null) {
            Set<Book> books = new HashSet<>(bookRepository.findAllById(authorDTO.getBookIds()));
            author.setBooks(books);
        }

        Author updatedAuthor = authorRepository.save(author);
        return convertToDTO(updatedAuthor);
    }

    @Transactional
    public void deleteAuthor(Long id, String token) {
        User requester = securityUtil.validateAndGetUserFromToken(token);
        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admins can delete authors");
        }

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        authorRepository.delete(author);
    }
    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setBiography(author.getBiography());

        if (author.getBooks() != null) {
            dto.setBookIds(author.getBooks().stream()
                    .map(Book::getId)
                    .collect(Collectors.toSet()));
        } else {
            dto.setBookIds(new HashSet<>());
        }

        dto.setCreatedAt(author.getCreatedAt());
        dto.setUpdatedAt(author.getUpdatedAt());
        return dto;
    }
}
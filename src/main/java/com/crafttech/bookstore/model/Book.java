package com.crafttech.bookstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int stockQuantity = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate publishedDate;

    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private Set<Author> authors = new HashSet<>();

    public void addAuthor(Author author) {
      this.authors.add(author);
      author.getBooks().add(this);
    }

    public void removeAuthor(Author author) {
      this.authors.remove(author);
      author.getBooks().remove(this);
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
package com.crafttech.bookstore.repository;

import com.crafttech.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors WHERE b.id = :id")
    Optional<Book> findById(@Param("id") Long id);

    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors WHERE LOWER(b.title) LIKE LOWER(concat('%', :title,'%'))")
    List<Book> findByTitle(@Param("title") String title);

    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors a WHERE a.id = :authorId")
    List<Book> findByAuthorId(@Param("authorId") Long authorId);

    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors")
    List<Book> findAllWithAuthors();
}
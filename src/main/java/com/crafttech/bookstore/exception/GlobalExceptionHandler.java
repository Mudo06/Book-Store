package com.crafttech.bookstore.exception;

import java.util.ConcurrentModificationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ConcurrentModificationException.class)
    public ResponseEntity<ErrorResponse> handleConcurrentModificationException(ConcurrentModificationException ex) {
        ErrorResponse error = new ErrorResponse(
            "CONCURRENT_MODIFICATION_ERROR",
            "Veri eşzamanlı değişiklik hatası. Lütfen tekrar deneyin."
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "Beklenmeyen bir hata oluştu: " + ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StackOverflowError.class)
    public ResponseEntity<ErrorResponse> handleStackOverflow(StackOverflowError ex) {
        ErrorResponse error = new ErrorResponse(
                "STACK_OVERFLOW",
                "Döngüsel bağımlılık tespit edildi. Lütfen veri yapınızı kontrol edin.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Data
    @AllArgsConstructor
    static class ErrorResponse {
        private String code;
        private String message;
    }
}
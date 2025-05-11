package com.crafttech.bookstore.service;

import com.crafttech.bookstore.config.JwtService;
import com.crafttech.bookstore.dto.Cart.CartItemDTO;
import com.crafttech.bookstore.dto.Cart.ShoppingCartDTO;
import com.crafttech.bookstore.exception.ResourceNotFoundException;
import com.crafttech.bookstore.exception.UnauthorizedException;
import com.crafttech.bookstore.model.*;
import com.crafttech.bookstore.model.Cart.CartItem;
import com.crafttech.bookstore.model.Cart.ShoppingCart;
import com.crafttech.bookstore.repository.BookRepository;
import com.crafttech.bookstore.repository.UserRepository;
import com.crafttech.bookstore.repository.Cart.ShoppingCartRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final JwtService jwtService;

    public ShoppingCartDTO getMyCart(String token) {
        Long userId = getUserIdFromToken(token);
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Shopping cart not found for user id: " + userId));
        return convertToDTO(cart);
    }

    @Transactional
    public ShoppingCartDTO addItemToCart(String token, CartItemDTO cartItemDTO) {
        Long userId = getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Book book = bookRepository.findById(cartItemDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + cartItemDTO.getBookId()));

        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(user);
                    return shoppingCartRepository.save(newCart);
                });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(cartItemDTO.getBookId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItemDTO.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setBook(book);
            newItem.setQuantity(cartItemDTO.getQuantity());
            cart.getItems().add(newItem);
        }

        ShoppingCart updatedCart = shoppingCartRepository.save(cart);
        return convertToDTO(updatedCart);
    }

    @Transactional
    public ShoppingCartDTO updateCartItem(String token, Long itemId, int quantity) {
        Long userId = getUserIdFromToken(token);
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Shopping cart not found for user id: " + userId));

        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + itemId));

        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }

        ShoppingCart updatedCart = shoppingCartRepository.save(cart);
        return convertToDTO(updatedCart);
    }

    @Transactional
    public void removeItemFromCart(String token, Long itemId) {
        Long userId = getUserIdFromToken(token);
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Shopping cart not found for user id: " + userId));

        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(itemId));
        if (removed) {
            shoppingCartRepository.save(cart);
        } else {
            throw new ResourceNotFoundException("Cart item not found with id: " + itemId);
        }
    }

    @Transactional
    public void clearCart(String token) {
        Long userId = getUserIdFromToken(token);
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Shopping cart not found for user id: " + userId));

        cart.getItems().clear();
        shoppingCartRepository.save(cart);
    }

    private Long getUserIdFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid token format");
        }
        
        String jwt = token.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));
        
        if (!jwtService.isTokenValid(jwt, user)) {
            throw new UnauthorizedException("Invalid or expired token");
        }
        
        return user.getId();
    }

    private ShoppingCartDTO convertToDTO(ShoppingCart cart) {
        ShoppingCartDTO dto = new ShoppingCartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setItems(cart.getItems().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList()));
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        return dto;
    }

    private CartItemDTO convertItemToDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setBookId(item.getBook().getId());
        dto.setBookTitle(item.getBook().getTitle());
        dto.setBookPrice(item.getBook().getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        return dto;
    }
}
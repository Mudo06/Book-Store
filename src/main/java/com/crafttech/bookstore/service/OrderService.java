package com.crafttech.bookstore.service;

import com.crafttech.bookstore.config.SecurityUtil;
import com.crafttech.bookstore.dto.Order.CreateOrderRequest;
import com.crafttech.bookstore.dto.Order.OrderDTO;
import com.crafttech.bookstore.dto.Order.OrderItemDTO;
import com.crafttech.bookstore.exception.ResourceNotFoundException;
import com.crafttech.bookstore.model.*;
import com.crafttech.bookstore.model.Cart.CartItem;
import com.crafttech.bookstore.model.Cart.ShoppingCart;
import com.crafttech.bookstore.model.Order.Order;
import com.crafttech.bookstore.model.Order.OrderItem;
import com.crafttech.bookstore.model.Order.OrderStatus;
import com.crafttech.bookstore.repository.Cart.CartItemRepository;
import com.crafttech.bookstore.repository.Cart.ShoppingCartRepository;
import com.crafttech.bookstore.repository.Order.OrderItemRepository;
import com.crafttech.bookstore.repository.Order.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
        private final OrderRepository orderRepository;
        private final OrderItemRepository orderItemRepository;
        private final ShoppingCartRepository shoppingCartRepository;
        private final CartItemRepository cartItemRepository;
        private final SecurityUtil securityUtil;

        @Transactional
        public OrderDTO createOrder(String token, CreateOrderRequest request) {
                User user = securityUtil.validateAndGetUserFromToken(token);

                ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Shopping cart not found for user id: " + user
                                                                .getId()));

                List<CartItem> cartItems = cartItemRepository.findAllById(request.getCartItemIds());
                if (cartItems.isEmpty()) {
                        throw new ResourceNotFoundException("No cart items found with provided ids");
                }

                // Calculate total price
                BigDecimal totalPrice = cartItems.stream()
                                .map(item -> BigDecimal.valueOf(item.getBook().getPrice()).multiply(BigDecimal.valueOf(
                                                item.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Create order
                Order order = Order.builder()
                                .user(user)
                                .orderDate(LocalDateTime.now())
                                .totalPrice(totalPrice)
                                .status(OrderStatus.PENDING)
                                .build();

                Order savedOrder = orderRepository.save(order);

                // Create order items
                List<OrderItem> orderItems = cartItems.stream()
                                .map(cartItem -> OrderItem.builder()
                                                .order(savedOrder)
                                                .book(cartItem.getBook())
                                                .quantity(cartItem.getQuantity())
                                                .unitPrice(BigDecimal.valueOf(cartItem.getBook().getPrice()))
                                                .build())
                                .collect(Collectors.toList());

                orderItemRepository.saveAll(orderItems);
                savedOrder.setItems(orderItems); // << BU SATIRI EKLE
                // Remove ordered items from cart
                cart.getItems().removeAll(cartItems);
                shoppingCartRepository.save(cart);

                return convertToDTO(savedOrder);
        }

        public List<OrderDTO> getUserOrders(String token) {
                User user = securityUtil.validateAndGetUserFromToken(token);

                return orderRepository.findByUser(user).stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        public OrderDTO getOrderDetails(String token, Long orderId) {
                User user = securityUtil.validateAndGetUserFromToken(token);
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Order not found with id: " + orderId));

                if (!order.getUser().getId().equals(user.getId())) {
                        throw new ResourceNotFoundException("Order not found for current user");
                }

                return convertToDTO(order);
        }

        private OrderDTO convertToDTO(Order order) {
                return OrderDTO.builder()
                                .id(order.getId())
                                .userId(order.getUser().getId())
                                .orderDate(order.getOrderDate())
                                .totalPrice(order.getTotalPrice())
                                .status(order.getStatus())
                                .items(order.getItems().stream()
                                                .map(this::convertItemToDTO)
                                                .collect(Collectors.toList()))
                                .createdAt(order.getCreatedAt())
                                .updatedAt(order.getUpdatedAt())
                                .build();
        }

        private OrderItemDTO convertItemToDTO(OrderItem item) {
                return OrderItemDTO.builder()
                                .id(item.getId())
                                .bookId(item.getBook().getId())
                                .bookTitle(item.getBook().getTitle())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .build();
        }
}
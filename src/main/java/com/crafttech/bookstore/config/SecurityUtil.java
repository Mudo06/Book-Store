package com.crafttech.bookstore.config;

import com.crafttech.bookstore.exception.ResourceNotFoundException;
import com.crafttech.bookstore.exception.UnauthorizedException;
import com.crafttech.bookstore.model.User;
import com.crafttech.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public User validateAndGetUserFromToken(String token) {
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

        return user;
    }
}

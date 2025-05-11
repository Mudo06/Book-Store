package com.crafttech.bookstore.service;

import com.crafttech.bookstore.config.JwtService;
import com.crafttech.bookstore.config.SecurityUtil;
import com.crafttech.bookstore.dto.UserDTO;
import com.crafttech.bookstore.dto.Jwt.AuthResponseDTO;
import com.crafttech.bookstore.exception.ResourceNotFoundException;
import com.crafttech.bookstore.exception.UnauthorizedException;
import com.crafttech.bookstore.model.User;
import com.crafttech.bookstore.model.User.Role;
import com.crafttech.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SecurityUtil securityUtil;

    public List<UserDTO> getAllUsers(String token) {
        User requester = securityUtil.validateAndGetUserFromToken(token);
        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admins can access all users");
        }
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id, String token) {
        User requester = securityUtil.validateAndGetUserFromToken(token);
        if (requester.getRole() != Role.ADMIN && !requester.getId().equals(id)) {
            throw new UnauthorizedException("You can only access your own user information");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setRole(Role.CUSTOMER);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Transactional
    public UserDTO updateCurrentUser(UserDTO userDTO, String token) {
        User currentUser = securityUtil.validateAndGetUserFromToken(token);

        // Sadece kendi bilgilerini güncelleyebilir
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + currentUser.getId()));

        user.setUsername(userDTO.getUsername());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setEmail(userDTO.getEmail());

        // Rolü değiştiremez (admin bile olsa kendi rolünü değiştiremez)
        user.setRole(currentUser.getRole());

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public UserDTO createAdmin(UserDTO userDTO, String token) {
        User requester = securityUtil.validateAndGetUserFromToken(token);
        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admins can create other admins");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setRole(Role.ADMIN);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Transactional
    public UserDTO updateAdmin(Long id, UserDTO userDTO, String token) {
        User requester = securityUtil.validateAndGetUserFromToken(token);
        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admins can update admin users");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setUsername(userDTO.getUsername());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id, String token) {
        User requester = securityUtil.validateAndGetUserFromToken(token);
        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admins can delete users");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public AuthResponseDTO signIn(String email, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Kullanıcı adı veya şifre hatalı!", ex);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        user.setToken(refreshToken);
        userRepository.save(user);

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
package com.payment.portal.service;

import com.payment.portal.dto.*;
import com.payment.portal.entity.User;
import com.payment.portal.repository.UserRepository;
import com.payment.portal.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    private static final List<String> ALLOWED_ROLES =
            List.of("user_web_portal", "admin");

    public UserResponse register(RegisterRequest req) {

        String role = (req.getRole() == null || req.getRole().isBlank())
                ? "user_web_portal"
                : req.getRole();

        if (!ALLOWED_ROLES.contains(role)) {
            throw new RuntimeException("Invalid role");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(PasswordUtil.hash(req.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);
        return toResponse(user);
    }

    public UserResponse login(LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!PasswordUtil.match(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        user.setAccessToken(tokenService.generate());
        userRepository.save(user);

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .accessToken(user.getAccessToken())
                .build();
    }
}
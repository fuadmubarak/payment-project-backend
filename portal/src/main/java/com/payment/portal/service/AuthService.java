package com.payment.portal.service;

import com.payment.portal.dto.LoginRequest;
import com.payment.portal.dto.RegisterRequest;
import com.payment.portal.dto.UserResponse;
import com.payment.portal.entity.Role;
import com.payment.portal.entity.User;
import com.payment.portal.exception.EmailAlreadyExistsException;
import com.payment.portal.repository.RoleRepository;
import com.payment.portal.repository.UserRepository;
import com.payment.portal.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;

    private static final String DEFAULT_ROLE_CODE = "user_web_portal";

    /**
     * REGISTER
     */
    public UserResponse register(RegisterRequest req) {

        // 1️⃣ Check existing email using Optional
        userRepository.findByEmail(req.getEmail())
                .ifPresent(u -> {
                    throw new EmailAlreadyExistsException("Email already registered");
                });

        // 2️⃣ Determine role code (default if not provided)
        String roleCode = (req.getRole() == null || req.getRole().isBlank())
                ? DEFAULT_ROLE_CODE
                : req.getRole();

        // 3️⃣ Fetch role from DB
        Role role = roleRepository.findByCode(roleCode)
                .orElseThrow(() -> new RuntimeException("Invalid role"));

        // 4️⃣ Create user entity
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(PasswordUtil.hash(req.getPassword()))
                .role(role)
                .build();
 
        // 5️⃣ Save user (DB unique constraint still protects us)
        userRepository.save(user);

        return toResponse(user);
    }

    /**
     * LOGIN
     */
    public UserResponse login(LoginRequest req) {

        // 1️⃣ Find user by email
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // 2️⃣ Validate password
        if (!PasswordUtil.match(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3️⃣ Generate and save access token
        user.setAccessToken(tokenService.generate());
        userRepository.save(user);

        return toResponse(user);
    }

    /**
     * ENTITY → DTO MAPPER
     */
    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getCode())
                .accessToken(user.getAccessToken())
                .build();
    }
}
package com.payment.portal.controller;

import com.payment.portal.dto.*;
import com.payment.portal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
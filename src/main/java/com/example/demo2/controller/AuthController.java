package com.example.demo2.controller;

import com.example.demo2.dto.request.LoginRequest;
import com.example.demo2.dto.request.RefreshTokenRequest;
import com.example.demo2.dto.request.RegisterRequest;
import com.example.demo2.dto.response.AuthResponse;
import com.example.demo2.model.entity.Role;
import com.example.demo2.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Register, Login, Refresh, Logout")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request.getEmail(), request.getPassword(), Role.ROLE_USER);
    }

    @Operation(summary = "Login and get JWT tokens")
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.authenticate(request.getEmail(), request.getPassword());
    }

    @Operation(summary = "Get new JWT tokens")
    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request.getRefreshToken());
    }
}
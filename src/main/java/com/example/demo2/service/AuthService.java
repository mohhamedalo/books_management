package com.example.demo2.service;

import com.example.demo2.dto.response.AuthResponse;
import com.example.demo2.model.entity.Role;
import com.example.demo2.model.entity.Token;
import com.example.demo2.model.entity.User;
import com.example.demo2.repository.TokenRepository;
import com.example.demo2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final TokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthResponse register(String email, String password, Role role) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();
        userRepo.save(user);

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, accessToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse authenticate(String email, String password) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = userRepo.findByEmail(email).orElseThrow();

        revokeAllUserTokens(user);

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, accessToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 🟢 Refresh token: sinh access_token mới từ refresh_token hợp lệ
    public AuthResponse refreshToken(String refreshToken) {
        var username = jwtService.extractUsername(refreshToken);
        var user = userRepo.findByEmail(username).orElseThrow();

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        var newAccessToken = jwtService.generateToken(user);
        saveUserToken(user, newAccessToken);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // giữ nguyên refresh token cũ
                .build();
    }

    // Lưu token mới
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepo.save(token);
    }

    // Revoke tất cả token cũ
    private void revokeAllUserTokens(User user) {
        var validTokens = tokenRepo.findAllByUserIdAndExpiredFalseAndRevokedFalse(user.getId());
        if (validTokens.isEmpty()) return;
        validTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepo.saveAll(validTokens);
    }
}

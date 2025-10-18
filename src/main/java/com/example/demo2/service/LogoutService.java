package com.example.demo2.service;

import com.example.demo2.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepo;

    public LogoutService(TokenRepository tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;

        String jwt = authHeader.substring(7);
        var storedToken = tokenRepo.findByToken(jwt).orElse(null);

        if (storedToken == null) {
            request.setAttribute("logoutStatus", "Token not found or already revoked");
            return;
        }

        if (storedToken.isExpired() || storedToken.isRevoked()) {
            request.setAttribute("logoutStatus", "Token already invalid");
            return;
        }

        storedToken.setExpired(true);
        storedToken.setRevoked(true);
        tokenRepo.save(storedToken);
        request.setAttribute("logoutStatus", "Logout successful");
    }
}
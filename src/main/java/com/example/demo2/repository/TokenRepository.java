package com.example.demo2.repository;

import com.example.demo2.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    List<Token> findAllByUserIdAndExpiredFalseAndRevokedFalse(Integer userId);
    Optional<Token> findByToken(String token);
}

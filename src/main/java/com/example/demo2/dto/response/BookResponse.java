package com.example.demo2.dto.response;

import java.time.Instant;

public record BookResponse(
        Integer id,
        String title,
        String status,
        Instant createdAt,
        Integer authorId,
        String authorFullName
) {}
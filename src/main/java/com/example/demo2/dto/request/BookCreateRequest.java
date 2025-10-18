package com.example.demo2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookCreateRequest(
        @NotNull Integer authorId,
        @NotBlank String title,
        @NotBlank String status
) {}

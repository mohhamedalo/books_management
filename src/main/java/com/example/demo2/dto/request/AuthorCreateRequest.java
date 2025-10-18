package com.example.demo2.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthorCreateRequest(
        @NotBlank String firstName,
        @NotBlank String lastName
) {}

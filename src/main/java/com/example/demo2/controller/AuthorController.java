package com.example.demo2.controller;

import com.example.demo2.dto.request.AuthorCreateRequest;
import com.example.demo2.dto.response.AuthorResponse;
import com.example.demo2.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> create(@RequestBody @Valid AuthorCreateRequest request) {
        AuthorResponse a = authorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(a);
    }
}

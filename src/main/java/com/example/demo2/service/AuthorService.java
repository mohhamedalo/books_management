package com.example.demo2.service;

import com.example.demo2.dto.request.AuthorCreateRequest;
import com.example.demo2.dto.response.AuthorResponse;
import com.example.demo2.exception.ResourceNotFoundException;
import com.example.demo2.model.entity.Author;
import com.example.demo2.repository.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorResponse create(AuthorCreateRequest request) {
        Author a = Author.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        a = authorRepository.save(a);

        return new AuthorResponse(a.getId(), a.getFirstName(), a.getLastName());
    }

    public Author getById(Integer authorId) {
        return authorRepository
                .findById(authorId)
                .orElseThrow(()-> new ResourceNotFoundException("Author not found" + authorId));
    }
}

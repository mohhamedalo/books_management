package com.example.demo2.mapper;

import com.example.demo2.dto.response.BookResponse;
import com.example.demo2.model.entity.Book;

public class BookMapper {
    public static BookResponse toResponse(Book b) {
        String fullName = b.getAuthor().getFirstName() + b.getAuthor().getLastName();
        return new BookResponse(
                b.getId(),
                b.getTitle(),
                b.getStatus(),
                b.getCreatedAt(),
                b.getAuthor().getId(),
                fullName
        );
    }
}

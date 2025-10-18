package com.example.demo2.service;

import com.example.demo2.dto.request.BookCreateRequest;
import com.example.demo2.dto.response.BookResponse;
import com.example.demo2.mapper.BookMapper;
import com.example.demo2.model.entity.Author;
import com.example.demo2.model.entity.Book;
import com.example.demo2.repository.BookRepository;
import com.example.demo2.spec.BookSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    public BookResponse create(BookCreateRequest request) {
        Author author = authorService.getById(request.authorId());
        Book b = Book.builder()
                .author(author)
                .title(request.title())
                .status(request.status())
                .build();
        b = bookRepository.save(b);

        return BookMapper.toResponse(b);
    }

//    public Page<BookResponse> getAll(Pageable pageable) {
//        return bookRepository.findAll(pageable).map(BookMapper::toResponse);
//    }

    public Page<BookResponse> getAllFiltered(
            String authorName,
            Instant startDate,
            Instant endDate,
            Pageable pageable
    ) {
        Specification<Book> spec = Specification.allOf(
                BookSpecification.filterAuthorName(authorName),
                BookSpecification.createdBetween(startDate, endDate)
        );

        return bookRepository.findAll(spec, pageable)
                .map(BookMapper::toResponse);

    }
}

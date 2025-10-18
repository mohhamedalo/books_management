package com.example.demo2.controller;

import com.example.demo2.dto.request.BookCreateRequest;
import com.example.demo2.dto.response.BookResponse;
import com.example.demo2.model.entity.Book;
import com.example.demo2.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.time.*;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestBody @Valid BookCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(request));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Pageable pageable) {

        // Parse yyyy-MM-dd → Instant
        Instant startInstant = null;
        Instant endInstant = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            if (startDate != null && !startDate.isBlank()) {
                LocalDate localStart = LocalDate.parse(startDate, formatter);
                startInstant = localStart.atStartOfDay(ZoneOffset.UTC).toInstant();
            }
            if (endDate != null && !endDate.isBlank()) {
                LocalDate localEnd = LocalDate.parse(endDate, formatter);
                // set đến cuối ngày 23:59:59.999
                endInstant = localEnd.plusDays(1).atStartOfDay(ZoneOffset.UTC).minusNanos(1).toInstant();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format, expected yyyy-MM-dd"));
        }

        Pageable effectivePageable = pageable;
        if (pageable.getSort().isUnsorted()) {
            effectivePageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }

        Page<BookResponse> page = bookService.getAllFiltered(
                authorName,
                startInstant,
                endInstant,
                pageable
        );

        // Tạo meta luôn luôn có, dù trống
        Map<String, Object> meta = Map.of(
                "page", page.getNumber(),
                "size", page.getSize(),
                "totalPages", page.getTotalPages(),
                "totalElements", page.getTotalElements(),
                "sort", effectivePageable.getSort().toString()
        );

        // data luôn tồn tại (list rỗng nếu không có phần tử)
        Map<String, Object> body = Map.of(
                "data", page.getContent(),
                "meta", meta
        );

        return ResponseEntity.ok(body);
    }

}

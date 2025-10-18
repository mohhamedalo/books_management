package com.example.demo2.spec;

import com.example.demo2.model.entity.Book;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class BookSpecification {
    public static Specification<Book> filterAuthorName(String authorName) {
        return (root, query, cb) -> {
            if (authorName == null || authorName.isBlank()) return null;
            // join sang bảng author để lọc theo firstName hoặc lastName
            var authorJoin = root.join("author");
            String likePattern = "%" + authorName.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(authorJoin.get("firstName")), likePattern),
                    cb.like(cb.lower(authorJoin.get("lastName")), likePattern)
            );
        };
    }

    public static Specification<Book> createdBetween(Instant start, Instant end) {
        return (root, query, cb) -> {
            if (start == null && end == null) return null;
            if (start != null && end != null)
                return cb.between(root.get("createdAt"), start, end);
            else if (start != null)
                return cb.greaterThanOrEqualTo(root.get("createdAt"), start);
            else
                return cb.lessThanOrEqualTo(root.get("createdAt"), end);
        };
    }
}

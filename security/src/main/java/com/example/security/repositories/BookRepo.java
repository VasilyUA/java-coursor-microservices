package com.example.security.repositories;

import com.example.security.entitys.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> {
    Book findByTitle(String title);
}

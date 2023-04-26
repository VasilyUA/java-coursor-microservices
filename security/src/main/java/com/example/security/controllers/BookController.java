package com.example.security.controllers;

import com.example.security.dtos.book.input.*;
import com.example.security.dtos.book.output.BookWithAuthorsDTO;
import com.example.security.services.BookService;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookWithAuthorsDTO> createBook(@RequestBody CreateBookDTO createBookDTO) {
        BookWithAuthorsDTO bookWithAuthorsDTO = bookService.createBook(createBookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookWithAuthorsDTO);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookWithAuthorsDTO> updateBook(@PathVariable Long bookId, @RequestBody UpdateBookDTO updateBookDTO) throws JsonMappingException {
        BookWithAuthorsDTO bookWithAuthorsDTO = bookService.updateBook(bookId, updateBookDTO);
        return ResponseEntity.ok(bookWithAuthorsDTO);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookWithAuthorsDTO> getBook(@PathVariable Long bookId) {
        BookWithAuthorsDTO bookWithAuthorsDTO = bookService.getBook(bookId);
        return ResponseEntity.ok(bookWithAuthorsDTO);
    }

    @GetMapping
    public ResponseEntity<List<BookWithAuthorsDTO>> getAllBooks() {
        List<BookWithAuthorsDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{\"message\": \"Book with id: " + bookId + " was deleted\"}");
    }
}

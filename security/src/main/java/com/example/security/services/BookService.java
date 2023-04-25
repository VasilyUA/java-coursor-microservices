package com.example.security.services;

import com.example.security.dtos.book.input.CreateBookDTO;
import com.example.security.dtos.book.input.UpdateBookDTO;
import com.example.security.dtos.book.output.BookWithAuthorsDTO;
import com.example.security.dtos.user.output.UserDTO;
import com.example.security.entitys.Book;
import com.example.security.entitys.User;
import com.example.security.exceptions.NotFoundException;
import com.example.security.repositories.BookRepo;
import com.example.security.repositories.UserRepo;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepo bookRepo;
    private final UserRepo userRepo;
    private final ObjectMapper objectMapper;

    public BookWithAuthorsDTO createBook(CreateBookDTO createBookDTO) {
        var user = userRepo.findById(createBookDTO.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author not found with id: " + createBookDTO.getAuthorId()));

        Book existingBook = bookRepo.findByTitle(createBookDTO.getTitle());
        if (existingBook != null) {
            throw new NotFoundException("Book already exists with title: " + createBookDTO.getTitle());
        }

        Book book = objectMapper.convertValue(createBookDTO, Book.class);
        book.setAuthor(user);
        Book savedBook = bookRepo.save(book);

        return convertToBookWithAuthorDto(savedBook);
    }

    public BookWithAuthorsDTO updateBook(Long bookId, UpdateBookDTO updateBookDTO) throws JsonMappingException {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + bookId));
        objectMapper.updateValue(book, updateBookDTO);
        Book updatedBook = bookRepo.save(book);
        return convertToBookWithAuthorDto(updatedBook);
    }

    public BookWithAuthorsDTO getBook(Long bookId) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + bookId));
        return convertToBookWithAuthorDto(book);
    }

    public List<BookWithAuthorsDTO> getAllBooks() {
        List<Book> books = bookRepo.findAll();
        return books.stream()
                .map(this::convertToBookWithAuthorDto)
                .collect(Collectors.toList());
    }

    public void deleteBook(Long bookId) {
        if (!bookRepo.existsById(bookId)) {
            throw new NotFoundException("Book not found with id: " + bookId);
        }
        bookRepo.deleteById(bookId);
    }

    private BookWithAuthorsDTO convertToBookWithAuthorDto(Book book) {
        User author = book.getAuthor();
        if (author == null) {
            return objectMapper.convertValue(book, BookWithAuthorsDTO.class);
        }

        UserDTO authorDto = convertToDto(author);
        book.setAuthor(null);

        BookWithAuthorsDTO bookWithAuthorsDTO = objectMapper.convertValue(book, BookWithAuthorsDTO.class);
        bookWithAuthorsDTO.setAuthor(authorDto);

        return bookWithAuthorsDTO;
    }

    private UserDTO convertToDto(User user) {
        return objectMapper.convertValue(user, UserDTO.class);
    }

}

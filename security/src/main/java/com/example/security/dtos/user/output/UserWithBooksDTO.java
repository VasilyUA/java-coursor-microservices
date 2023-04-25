package com.example.security.dtos.user.output;

import com.example.security.dtos.book.output.BookDTO;
import lombok.*;

import java.util.*;

@Getter
@Setter
public class UserWithBooksDTO {
    private Long id;
    private String email;
    private List<BookDTO> books;
}

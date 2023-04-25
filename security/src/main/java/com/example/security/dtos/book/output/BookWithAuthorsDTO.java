package com.example.security.dtos.book.output;

import com.example.security.dtos.user.output.UserDTO;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookWithAuthorsDTO {
    private Long id;
    private String title;
    private String price;
    private UserDTO author;
}
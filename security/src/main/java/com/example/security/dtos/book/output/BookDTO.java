package com.example.security.dtos.book.output;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String price;
}

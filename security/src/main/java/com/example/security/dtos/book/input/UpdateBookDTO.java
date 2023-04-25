package com.example.security.dtos.book.input;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class UpdateBookDTO {
    @Size(max = 255, message = "Title should be no longer than 255 characters")
    private String title;

    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "Price should be a valid number with up to 2 decimal places")
    private String price;

    private Long authorId;
}

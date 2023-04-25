package com.example.security.dtos.book.input;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class CreateBookDTO {
    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title should be no longer than 255 characters")
    private String title;

    @NotBlank(message = "Price is mandatory")
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "Price should be a valid number with up to 2 decimal places")
    private String price;

    @NotNull(message = "Author ID is mandatory")
    private Long authorId;
}

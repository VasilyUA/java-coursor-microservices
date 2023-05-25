package com.example.apachekafka.dtos.news;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}

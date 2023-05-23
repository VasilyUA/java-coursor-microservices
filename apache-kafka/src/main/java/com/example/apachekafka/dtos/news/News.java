package com.example.apachekafka.dtos.news;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class News {
    private String title;
    private String content;
}

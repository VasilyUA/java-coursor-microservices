package com.example.apachekafka.repository;


import com.example.apachekafka.documents.News;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NewsRepo extends MongoRepository<News, String>  {
    News findNewsByTitle(String title);
}
package com.example.nosqldb.restclients.cat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "remote-cat-service", url = "https://catfact.ninja")
public interface CatServiceClient {
    @GetMapping(value = "/fact", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getCatFact();
}

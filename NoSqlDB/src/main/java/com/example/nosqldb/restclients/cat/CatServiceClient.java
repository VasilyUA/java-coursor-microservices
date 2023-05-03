package com.example.nosqldb.restclients.cat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "remote-cat-service", url = "${app.remote-cat-service.url}")
public interface CatServiceClient {
    @GetMapping(value = "/fact")
    ResponseEntity<String> getCatFact();
}

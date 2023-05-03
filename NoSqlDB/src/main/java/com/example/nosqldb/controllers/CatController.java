package com.example.nosqldb.controllers;

import com.example.nosqldb.restclients.cat.CatServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class CatController {

    @Autowired
    private CatServiceClient catServiceClient;

    @GetMapping(value = "cat/fact", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCatFact() {
        return catServiceClient.getCatFact();
    }
}

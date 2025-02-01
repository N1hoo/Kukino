package com.n1hoo.przepisy.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaviconController {

    @GetMapping("favicon.ico")
    public ResponseEntity<Void> favicon() {
        // Zwracamy 204 No Content – przeglądarka nie otrzyma żadnych danych
        return ResponseEntity.noContent().build();
    }
}

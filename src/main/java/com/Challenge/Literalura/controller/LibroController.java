package com.Challenge.Literalura.controller;

import com.Challenge.Literalura.model.Libro;
import com.Challenge.Literalura.service.LibroService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class LibroController {
    @Autowired
    private LibroService libroService;

    @PostMapping("/search")
    public Libro searchBook(@RequestParam String title) throws JsonProcessingException {
        return libroService.fetchAndSaveBook(title);
    }

    @GetMapping
    public List<Libro> getAllBooks() {
        return libroService.getAllBooks();
    }

    @GetMapping("/language")
    public List<Libro> getBooksByLanguage(@RequestParam String language) {
        return libroService.getBooksByLanguage(language);
    }
}

package com.Challenge.Literalura.repository;

import com.Challenge.Literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByLanguage(String language);
    Optional<Libro> findByTitleAndAutorName(String title, String autorName); // Añadir este método
}
